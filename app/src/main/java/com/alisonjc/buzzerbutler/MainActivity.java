package com.alisonjc.buzzerbutler;


import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ActionBar mActionBar;
    private String mUserName;
    private String mUserEmail;
    private static final String TAG = "MainActivity";
    private SharedPreferences mSharedPreferences;
    public static final String PREFS_FILE = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSharedPreferences = getApplicationContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);

        if(!mSharedPreferences.contains("username") || !mSharedPreferences.contains("email")){
            userLogin();
        }

        toolbarSetup();
        navigationDrawerSetup();
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
                mSharedPreferences.edit().remove("username");
                mSharedPreferences.edit().remove("email");

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

        if (id == R.id.profile_drawer) {

            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ProfileFragment playlistFragment = ProfileFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.main_framelayout, playlistFragment, "playlistFragment").addToBackStack(null).commit();
            mActionBar.setTitle(R.string.profile_drawer);
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

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

    }
}

