package com.example.projectmoveek.fragment_intro;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.projectmoveek.MainActivity;
import com.example.projectmoveek.R;


public class OnBoardingFragment1 extends Fragment {

    private LottieAnimationView mLottieAnim;
    private TextView tvSkip, tvTitle, tvSlogan, tvDescription;
    private Animation topAnim, botAnim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_on_boarding_1, container, false);

        mLottieAnim = root.findViewById(R.id.lottie_anim_1);
        tvSkip = root.findViewById(R.id.tv_skip);
        tvTitle = root.findViewById(R.id.tv_title);
        tvSlogan = root.findViewById(R.id.tv_slogan);
        tvDescription = root.findViewById(R.id.tv_description);

        // Animations
        topAnim = AnimationUtils.loadAnimation(getContext(), R.anim.top_animation);
        botAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_animation);

        tvSkip.setAnimation(topAnim);
        tvTitle.setAnimation(botAnim);
        tvSlogan.setAnimation(botAnim);
        tvDescription.setAnimation(botAnim);

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