package com.example.projectmoveek.fragment_intro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.example.projectmoveek.MainActivity;
import com.example.projectmoveek.R;

public class OnBoardingFragment2 extends Fragment {

    private LottieAnimationView mLottieAnim;
    private TextView tvSkip, tvTitle, tvSlogan, tvDescription;
    private Animation topAnim, botAnim;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_on_boarding_2, container, false);

        mLottieAnim = root.findViewById(R.id.lottie_anim_2);
        mLottieAnim.bringToFront();

        tvSkip = root.findViewById(R.id.tv_skip);

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}
