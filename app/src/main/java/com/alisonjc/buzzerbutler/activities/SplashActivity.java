package com.alisonjc.buzzerbutler.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.alisonjc.buzzerbutler.BuildConfig;
import com.alisonjc.buzzerbutler.R;
import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class SplashActivity extends AwesomeSplash {

    private static final String TAG = "SplashActivity";


    // DO NOT OVERRIDE onCreate()! Use initSplash for any additional setup code.
    @Override
    public void initSplash(ConfigSplash configSplash) {

        final int duration;
        // Duration for splash screen animations.
        if (BuildConfig.DEBUG) {
            duration = 500;
        } else {
            duration = 1000;
        }

        Log.d(TAG, "Splash duration: " + duration);

        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.color_white); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(duration); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_LEFT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_TOP); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.buzzer_butler_165); //or any other drawable
        configSplash.setAnimLogoSplashDuration(duration); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeInDown); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        //Customize Title -- Currently off.
        configSplash.setTitleSplash(getString(R.string.app_slogan));
        configSplash.setTitleTextColor(R.color.black);
        configSplash.setTitleTextSize(24f); //float value
        configSplash.setAnimTitleDuration(duration);
        configSplash.setAnimTitleTechnique(Techniques.BounceInLeft);
//         configSplash.setTitleFont("fonts/myfont.ttf"); //provide string to your font located in assets/fonts/
    }

    @Override
    public void animationsFinished() {
        // Launch next activity depending on login status.
        final Intent intent;
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
