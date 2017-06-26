package com.alisonjc.buzzerbutler.tests;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import com.alisonjc.buzzerbutler.R;
import com.alisonjc.buzzerbutler.activities.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityScreenTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity activity;


    @Before
    public void setup() {
        activity = mainActivityTestRule.launchActivity(new Intent());
    }

    @Test
    public void testToolbar() throws Exception{
        Espresso.onView(withId(R.id.toolbar))
                .check(matches(isDisplayed()));
    }

}
