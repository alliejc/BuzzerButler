package com.example.alisonjc.compplayertwo;


import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.alisonjc.compplayertwo.playlists.OnPlaylistInteractionListener;
import com.example.alisonjc.compplayertwo.playlists.PlaylistFragment;
import com.example.alisonjc.compplayertwo.spotify.SpotifyService;
import com.example.alisonjc.compplayertwo.spotify.model.playlists.SpotifyUser;
import com.example.alisonjc.compplayertwo.tracks.OnControllerTrackChangeListener;
import com.example.alisonjc.compplayertwo.tracks.OnTrackSelectedListener;
import com.example.alisonjc.compplayertwo.tracks.PlaylistTracksFragment;
import com.example.alisonjc.compplayertwo.tracks.TracksFragment;
import com.google.inject.Inject;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActionBarActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnPlaylistInteractionListener, OnControllerTrackChangeListener, OnTrackSelectedListener {

    @Inject
    SpotifyService mSpotifyService;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ActionBar mActionBar;
    private String mPlaylistTitle;
    private String mUserName;
    private String mUserEmail;
    private MediaControllerListener mMediaController;
    private PlaylistTracksFragment mPlaylistTracksFragment;
    private TracksFragment mTracksFragment;
    private static final int REQUEST_CODE = 1337;
    private OnControllerTrackChangeListener mOnControllerTrackChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (mSpotifyService.isLoggedIn()) {
            userLogin();
        }

        toolbarSetup();
    }

    private void navigationDrawerSetup() {

        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
        View header = mNavigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.nav_header_top);
        TextView email = (TextView) header.findViewById(R.id.nav_header_bottom);
        name.setText(mUserName);
        email.setText(mUserEmail);
    }

    @Override
    public void onBackPressed() {

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);

        } else if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStackImmediate();

        } else if (fragmentManager.getBackStackEntryCount() <= 1) {
            moveTaskToBack(true);

        } else {
            super.onBackPressed();
        }
    }

    private void userLogin() {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment dialogFragment = LoginDialogFrag.newInstance();
        dialogFragment.show(ft, "dialog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_overflow, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_logout:
                mSpotifyService.userLogout(getApplicationContext());
                userLogin();
                break;

            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_playlists) {

            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            PlaylistFragment playlistFragment = PlaylistFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.main_framelayout, playlistFragment, "playlistFragment").addToBackStack(null).commit();
            mActionBar.setTitle(R.string.playlists_drawer);

        } else if (id == R.id.nav_songs) {

            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            mTracksFragment = TracksFragment.newInstance();
            mOnControllerTrackChangeListener = mTracksFragment;
            fragmentManager.beginTransaction().replace(R.id.main_framelayout, mTracksFragment, "tracksFragment").addToBackStack(null).commit();
            mActionBar.setTitle(R.string.songs_drawer);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void toolbarSetup() {

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setTitle(R.string.app_name);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse authResponse = AuthenticationClient.getResponse(resultCode, intent);
            switch (authResponse.getType()) {

                case TOKEN:
                    final String mToken = authResponse.getAccessToken();
                    mSpotifyService.getCurrentUser(mToken).enqueue(new Callback<SpotifyUser>() {

                        @Override
                        public void onResponse(Call<SpotifyUser> call, Response<SpotifyUser> response) {
                            if (response.isSuccess()) {

                                mSpotifyService.setUserId(response.body().getId(), getBaseContext());
                                mSpotifyService.setToken(mToken, getBaseContext());
                                mUserName = response.body().getDisplayName();
                                mUserEmail = response.body().getEmail();
                                navigationDrawerSetup();

                                FragmentManager fragmentManager = getSupportFragmentManager();
                                PlaylistFragment playlistFragment = PlaylistFragment.newInstance();
                                mMediaController = MediaControllerListener.newInstance();

                                fragmentManager.beginTransaction()
                                        .replace(R.id.main_framelayout, playlistFragment, "playlistTracksFragment").addToBackStack(null)
                                        .commit();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.media_controls_frame, mMediaController, "mediaController")
                                        .commit();
                            }

                            mActionBar.setTitle(R.string.playlists_drawer);
                        }

                    @Override
                    public void onFailure (Call < SpotifyUser > call, Throwable t){
                }
            });
            break;

            case ERROR:
                break;

            default:
        }
    }
}

//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        //mActionBarDrawerToggle.onConfigurationChanged(newConfig);
//        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
//            setContentView(R.layout.content_main);
//            Log.e("On Config Change","LANDSCAPE");
//        }else {
//
//            Log.e("On Config Change","PORTRAIT");
//        }
//
//    }

    @Override
    public void onPlaylistSelected(String userId, String playlistId, String playlistTitle) {

        mPlaylistTitle = playlistTitle;
        mActionBar.setTitle(mPlaylistTitle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mPlaylistTracksFragment = PlaylistTracksFragment.newInstance(userId, playlistId);
        mOnControllerTrackChangeListener = mPlaylistTracksFragment;
        fragmentManager.beginTransaction().replace(R.id.main_framelayout, mPlaylistTracksFragment, "playlistTracksFragment").addToBackStack(null).commit();
    }

    @Override
    public void onTrackSelected(String songName, String artistName, String uri) {
        mMediaController.playSong(songName, artistName, uri); }

    @Override
    public void onControllerTrackChange(boolean skipforward) {
        if (mOnControllerTrackChangeListener != null) {
            mOnControllerTrackChangeListener.onControllerTrackChange(skipforward);
        }
    }
}

