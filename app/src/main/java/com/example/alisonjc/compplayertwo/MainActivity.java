package com.example.alisonjc.compplayertwo;


import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
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

import com.example.alisonjc.compplayertwo.playlists.PlaylistFragment;
import com.example.alisonjc.compplayertwo.spotify.SpotifyService;
import com.example.alisonjc.compplayertwo.spotify.model.playlists.SpotifyUser;
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
        implements NavigationView.OnNavigationItemSelectedListener, PlaylistFragment.PlaylistInteractionListener, PlaylistTracksFragment.OnPlaylistTracksInteractionListener, TracksFragment.OnTracksInteractionListener {

    @Inject
    SpotifyService mSpotifyService;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    NavigationView mNavigationView;

    private ActionBarDrawerToggle toggle;

    private ActionBar actionBar;
    private String mPlaylistTitle;

    private static final int REQUEST_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        navigationDrawerSetup();
        toolbarSetup();

        if (mSpotifyService.isLoggedIn()) {
            userLogin();
        }
    }

    private void navigationDrawerSetup() {

        toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
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
            actionBar.setTitle(R.string.playlists_drawer);
            actionBar.setSubtitle(R.string.app_subtitle);


        } else if (id == R.id.nav_songs) {

            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            TracksFragment tracksFragment = TracksFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.main_framelayout, tracksFragment, "tracksFragment").addToBackStack(null).commit();
            actionBar.setTitle(R.string.songs_drawer);
            actionBar.setSubtitle("Please select a song");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void toolbarSetup() {

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
        actionBar.setSubtitle(R.string.app_subtitle);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
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

                                FragmentManager fragmentManager = getSupportFragmentManager();
                                PlaylistFragment playlistFragment = PlaylistFragment.newInstance();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.main_framelayout, playlistFragment, "playlistTracksFragment").addToBackStack(null)
                                        .commit();

                                actionBar.setTitle(R.string.playlists_drawer);
                            }
                        }

                        @Override
                        public void onFailure(Call<SpotifyUser> call, Throwable t) {
                        }
                    });
                    break;

                case ERROR:
                    break;

                default:
            }
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPlaylistSelected(String userId, String playlistId, String playlistTitle) {

        mPlaylistTitle = playlistTitle;
        actionBar.setTitle(mPlaylistTitle);
        actionBar.setSubtitle("Please select a song");

        FragmentManager fragmentManager = getSupportFragmentManager();
        PlaylistTracksFragment playlistTracksFragment = PlaylistTracksFragment.newInstance(userId, playlistId);
        fragmentManager.beginTransaction().replace(R.id.main_framelayout, playlistTracksFragment, "playlistTracksFragment").addToBackStack(null).commit();
    }

    public void onPlaylistTrackSelected(String trackName) {
        actionBar.setSubtitle(trackName);
    }


    @Override
    public void onTrackSelected(String trackId) {
        actionBar.setSubtitle(trackId);
    }
}
