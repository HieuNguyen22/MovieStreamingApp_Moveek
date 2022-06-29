package com.example.projectmoveek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.projectmoveek.fragment_intro.OnBoardingFragment;
import com.example.projectmoveek.fragment_intro.SplashScreenFragment;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);

        setUpSplashScreen();
        setUpOnBoardingScreen();
    }

    private void setUpOnBoardingScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentManager fm_OnBoarding = getSupportFragmentManager();
                FragmentTransaction ft_OnBoarding = fm_OnBoarding.beginTransaction();

                ft_OnBoarding = fm_OnBoarding.beginTransaction();
                ft_OnBoarding.replace(R.id.fragment_container, new OnBoardingFragment());
                ft_OnBoarding.commit();
            }
        },2700);
    }

    private void setUpSplashScreen() {
        FragmentManager fm_splash_screen = getSupportFragmentManager();
        FragmentTransaction ft_splash_screen = fm_splash_screen.beginTransaction();

        ft_splash_screen = fm_splash_screen.beginTransaction();
        ft_splash_screen.add(R.id.fragment_container, new SplashScreenFragment());
        ft_splash_screen.commit();
    }
}