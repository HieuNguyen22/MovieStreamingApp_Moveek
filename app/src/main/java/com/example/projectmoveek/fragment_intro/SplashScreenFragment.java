package com.example.projectmoveek.fragment_intro;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.projectmoveek.R;


public class SplashScreenFragment extends Fragment {

    private Animation topAnim, botAnim;
    private TextView tvLogo, tvSlogan, tvDevelop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_splash_screen, container, false);

        // Animations
        topAnim = AnimationUtils.loadAnimation(getContext(), R.anim.top_animation);
        botAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_animation);

        tvLogo = view.findViewById(R.id.tv_logo);
        tvSlogan = view.findViewById(R.id.tv_slogan);
        tvDevelop = view.findViewById(R.id.tv_develop);

        tvLogo.setAnimation(topAnim);
        tvSlogan.setAnimation(botAnim);
        tvDevelop.setAnimation(botAnim);

        return view;
    }
}