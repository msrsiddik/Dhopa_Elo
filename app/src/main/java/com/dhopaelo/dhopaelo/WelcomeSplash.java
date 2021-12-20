package com.dhopaelo.dhopaelo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.dhopaelo.dhopaelo.databinding.ActivitySplashBinding;
import com.dhopaelo.dhopaelo.databinding.ActivityWelcomeSplashBinding;
import com.dhopaelo.dhopaelo.service.BannerService;
import com.dhopaelo.dhopaelo.text.drawable.ColorGenerator;
import com.dhopaelo.dhopaelo.ui.login.LoginActivity;

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
        int randomColor2 = ColorGenerator.MATERIAL.getRandomColor();
        binding.splashBackground.setBackgroundColor(randomColor);
        binding.appName.setTextColor(randomColor2);

        //binding.appName.animate().translationY(-1000f).setDuration(1500).setStartDelay(500);
        binding.lottie.animate().translationX(2000f).setDuration(1000).setStartDelay(1900);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(WelcomeSplash.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_TIME);
    }
}