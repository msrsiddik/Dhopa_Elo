package com.robotechvalley.dhopaelo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.robotechvalley.dhopaelo.databinding.ActivitySplashBinding;
import com.robotechvalley.dhopaelo.databinding.ActivityWelcomeSplashBinding;
import com.robotechvalley.dhopaelo.service.BannerService;
import com.robotechvalley.dhopaelo.text.drawable.ColorGenerator;
import com.robotechvalley.dhopaelo.ui.login.LoginActivity;

public class WelcomeSplash extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private static final int SPLASH_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        startService(new Intent(this, BannerService.class));

        int randomColor = ColorGenerator.MATERIAL.getRandomColor();
        binding.splashBackground.setBackgroundColor(randomColor);

        binding.appName.animate().translationY(-1000f).setDuration(1500).setStartDelay(500);
        binding.lottie.animate().translationX(2000f).setDuration(1000).setStartDelay(1900);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(WelcomeSplash.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_TIME);
    }
}