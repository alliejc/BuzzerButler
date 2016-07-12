package com.example.alisonjc.compplayertwo;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
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

import com.example.alisonjc.compplayertwo.spotify.SpotifyService;
import com.example.alisonjc.compplayertwo.spotify.model.playlists.SpotifyUser;
import com.google.inject.Inject;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActionBarActivity
        implements NavigationView.OnNavigationItemSelectedListener, PlaylistFragment.PlaylistInteractionListener, TracksFragment.OnTracksInteractionListener {

    @Inject
    SpotifyService mSpotifyService;

    @InjectView(R.id.toolbar)
    private Toolbar mToolbar;

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    NavigationView mNavigationView;

    private ActionBarDrawerToggle toggle;

    private ActionBar actionBar;


    private static final int REQUEST_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigationDrawerSetup();
        toolbarSetup();

        if(mSpotifyService.isLoggedIn()){
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
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_playlists) {
            selectItem(0);

        } else if (id == R.id.nav_songs) {
            selectItem(1);

        } else if (id == R.id.nav_artists) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void selectItem(int position) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position) {
            case 0:
                PlaylistFragment playlistFragment = PlaylistFragment.newInstance();
                fragmentManager.beginTransaction().replace(R.id.main_framelayout, playlistFragment).addToBackStack(null).commit();
                actionBar.setTitle(R.string.playlists_drawer);
                break;

            case 1:
                SongsFragment songsFragment = SongsFragment.newInstance();
                fragmentManager.beginTransaction().replace(R.id.main_framelayout, songsFragment).addToBackStack(null).commit();
                actionBar.setTitle(R.string.songs_drawer);
                break;

        }

    }

    private void toolbarSetup() {

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(R.string.app_name);
        actionBar.setSubtitle(R.string.app_subtitle);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setHomeAsUpIndicator(R.drawable.ic_action_music_white);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
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
                                mSpotifyService.setUserId(response.body().getId());


                                PlaylistFragment playlistFragment = new PlaylistFragment();
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.main_framelayout, playlistFragment).addToBackStack(null)
                                        .commit();

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
    public void onPlaylistSelected(String playlistId) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        TracksFragment tracksFragment = TracksFragment.newInstance(playlistId);
        fragmentManager.beginTransaction().replace(R.id.main_framelayout, tracksFragment).addToBackStack(null).commit();
    }

    @Override
    public void onTrackSelected(Uri uri) {

    }

}
