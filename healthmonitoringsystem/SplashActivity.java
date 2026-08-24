package com.harshdi.healthmonitoringsystem;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 seconds
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

        // Delay and check authentication
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            checkUserAuthentication();
        }, SPLASH_DELAY);
    }

    private void checkUserAuthentication() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        Intent intent;
        if (currentUser != null) {
            // User is logged in, go to dashboard
            intent = new Intent(SplashActivity.this, DashboardActivity.class);
        } else {
            // User is not logged in, go to main activity
            intent = new Intent(SplashActivity.this, MainActivity.class);
        }

        startActivity(intent);
        finish();
    }
}