package com.alisonjc.buzzerbutler.activities;


import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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

<<<<<<< HEAD:app/src/main/java/com/alisonjc/buzzerbutler/MainActivity.java
import com.alisonjc.buzzerbutler.helpers.SavedUserFragment;
=======
import com.alisonjc.buzzerbutler.fragments.LoginDialogFragment;
import com.alisonjc.buzzerbutler.ProfileFragment;
import com.alisonjc.buzzerbutler.R;
>>>>>>> add aws library:app/src/main/java/com/alisonjc/buzzerbutler/activities/MainActivity.java

import butterknife.BindView;
<<<<<<< HEAD
=======
import butterknife.ButterKnife;
>>>>>>> update background color

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

//    @BindView(R.id.drawer_layout)
//    DrawerLayout mDrawerLayout;
//
//    @BindView(R.id.nav_view)
//    NavigationView mNavigationView;

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ActionBar mActionBar;
    private String mUserName;
    private String mUserEmail;
    private static final String TAG = "MainActivity";
    private SharedPreferences mSharedPreferences;
    public static final String PREFS_FILE = "MyPrefsFile";
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = getApplicationContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);

        userLogin();

        toolbarSetup();
        navigationDrawerSetup();
    }

    private void navigationDrawerSetup() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

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
        DialogFragment dialogFragment = LoginDialogFragment.newInstance();
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
                mSharedPreferences.edit().remove("username").apply();
                mSharedPreferences.edit().remove("email").apply();

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

<<<<<<< HEAD:app/src/main/java/com/alisonjc/buzzerbutler/MainActivity.java
        switch (id) {

            case R.id.profile_drawer:
                addFragmentOnTop(ProfileFragment.newInstance());
                mActionBar.setTitle(R.string.profile_drawer);
                break;

            case R.id.saved_drawer:
                addFragmentOnTop(SavedUserFragment.newInstance());
                mActionBar.setTitle(R.string.saved_drawer);
                break;

                default:
                    break;


        }

=======
>>>>>>> add aws library:app/src/main/java/com/alisonjc/buzzerbutler/activities/MainActivity.java
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addFragmentOnTop(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);

        fragmentManager
                .beginTransaction()
                .replace(R.id.main_framelayout, fragment)
                .addToBackStack(BACK_STACK_ROOT_TAG)
                .commit();
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

