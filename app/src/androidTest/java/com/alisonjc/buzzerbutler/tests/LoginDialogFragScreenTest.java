package com.alisonjc.buzzerbutler.tests;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.alisonjc.buzzerbutler.R;
import com.alisonjc.buzzerbutler.activities.MainActivity;
import com.alisonjc.buzzerbutler.fragments.LoginDialogFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class LoginDialogFragScreenTest {


    @Rule
    public ActivityTestRule<MainActivity> loginDialogTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    private LoginDialogFragment fragment;
    private MainActivity activity;


    @Before
    public void setup() {
        activity = loginDialogTestRule.launchActivity(new Intent());
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        fragment = new LoginDialogFragment();
        fragment.show(ft, "LoginDialog");
    }

    @Test
    public void emailEditTextTest() {
        Espresso.onView(withId(R.id.email_login))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(typeText("email@email.com"))
                .check(matches(withText("email@email.com")));
    }

    @Test
    public void passEditTextTest() {

                Espresso.onView(withId(R.id.pass_login))
                        .inRoot(isDialog())
                        .check(matches(isDisplayed()))
                .perform(typeText("password"))
                .check(matches(withText("password")));
    }

    @Test
    public void loginButtonTest() {

        Espresso.onView(withId(R.id.button_login))
                .check(matches(isDisplayed()));
    }

}
