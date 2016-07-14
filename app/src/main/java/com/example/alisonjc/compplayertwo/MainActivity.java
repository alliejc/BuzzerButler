package com.example.alisonjc.compplayertwo;


import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
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
        implements NavigationView.OnNavigationItemSelectedListener, PlaylistFragment.PlaylistInteractionListener, PlaylistTracksFragment.OnPlaylistTracksInteractionListener, TracksFragment.OnTracksInteractionListener {

    @Inject
    SpotifyService mSpotifyService;

    @InjectView(R.id.toolbar)
    private Toolbar mToolbar;

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    NavigationView mNavigationView;

    private ActionBarDrawerToggle toggle;

    private ActionBar actionBar;
    private String mPlaylistTitle;


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

        toolbarSetup();
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

            case R.id.sort_a_z:

                return true;

            case R.id.sort_z_a:

                return true;

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
        FragmentManager fragmentManager = getSupportFragmentManager();


        if (id == R.id.nav_playlists) {

            PlaylistFragment playlistFragment = PlaylistFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.main_framelayout, playlistFragment, "playlistFragment").addToBackStack(null).commit();
            actionBar.setTitle(R.string.playlists_drawer);

        } else if (id == R.id.nav_songs) {

            TracksFragment tracksFragment = TracksFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.main_framelayout, tracksFragment).addToBackStack(null).commit();
            actionBar.setTitle(R.string.songs_drawer);
            toolbarSetup();

        } else if (id == R.id.nav_artists) {

        } else if (id == R.id.nav_logout) {

            //userLogout();

        } else if (id == R.id.nav_send) {

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

                                mSpotifyService.setUserId(response.body().getId());

                                getPreferences(Context.MODE_PRIVATE).edit().putString("user", response.body().getId()).apply();
                                getPreferences(Context.MODE_PRIVATE).edit().putString("token", mToken).apply();

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
    public void onPlaylistSelected(String playlistId, String playlistTitle) {

        mPlaylistTitle = playlistTitle;
        actionBar.setTitle(mPlaylistTitle);
        actionBar.setSubtitle("Please select a song");

        FragmentManager fragmentManager = getSupportFragmentManager();
        PlaylistTracksFragment playlistTracksFragment = PlaylistTracksFragment.newInstance(playlistId);
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
