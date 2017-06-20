package com.alisonjc.buzzerbutler.activities;


import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.alisonjc.buzzerbutler.UserItem;
import com.alisonjc.buzzerbutler.fragments.AddUserFragment;
import com.alisonjc.buzzerbutler.fragments.SavedUserFragment;
import com.alisonjc.buzzerbutler.fragments.LoginDialogFragment;
import com.alisonjc.buzzerbutler.fragments.ProfileFragment;
import com.alisonjc.buzzerbutler.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SavedUserFragment.OnSavedUserInteractionListener, AddUserFragment.OnAddUserInteraction, ProfileFragment.OnFragmentInteractionListener, LoginDialogFragment.OnCompleteListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    private TextView name;
    private TextView email;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ActionBar mActionBar;
    private static final String TAG = "MainActivity";
    private SharedPreferences mSharedPreferences;
    private SavedUserFragment mSavedUserFragment;
    public static final String PREFS_FILE = "MyPrefsFile";
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSharedPreferences = getApplicationContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);

        final String userEmail = mSharedPreferences.getString("email", null);
        if (userEmail != null) {
            userLogin();
        }

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

        name = (TextView) header.findViewById(R.id.nav_header_top);
        email = (TextView) header.findViewById(R.id.nav_header_bottom);
        name.setText(mSharedPreferences.getString("name", null));
        email.setText(mSharedPreferences.getString("email", null));
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
        dialogFragment.show(ft, "LoginDialog");

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
                removeSharedPreferences();
                userLogin();
                break;

            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void removeSharedPreferences(){
        mSharedPreferences.edit().remove("email").apply();
        mSharedPreferences.edit().remove("pass").apply();
        mSharedPreferences.edit().remove("name").apply();
        mSharedPreferences.edit().remove("phone_number").apply();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.profile_drawer:
                addFragmentOnTop(ProfileFragment.newInstance());
                mActionBar.setTitle(R.string.profile_drawer);
                break;

            case R.id.saved_drawer:
                mSavedUserFragment = SavedUserFragment.newInstance();
                addFragmentOnTop(mSavedUserFragment);
                mActionBar.setTitle(R.string.saved_drawer);
                break;

            case R.id.add_drawer:
                addFragmentOnTop(AddUserFragment.newInstance());
                mActionBar.setTitle(R.string.add_drawer);
                break;
            default:
                break;
        }

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

    @Override
    public void onAddUserInteraction(UserItem item) {
        mSavedUserFragment.addItem(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSavedUserInteraction() {

    }

    @Override
    public void onComplete() {
        addFragmentOnTop(ProfileFragment.newInstance());
    }
}

