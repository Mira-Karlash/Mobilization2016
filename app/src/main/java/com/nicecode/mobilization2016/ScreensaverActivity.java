package com.nicecode.mobilization2016;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ScreensaverActivity extends AppCompatActivity {

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_screensaver);

        Thread splash_time = new Thread() {
            public void run() {
                try {
                    int SplashTimer = 0;
                    while (SplashTimer < 3000) {
                        sleep(100);
                        SplashTimer = SplashTimer + 100;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                    startActivity(new Intent(ScreensaverActivity.this, MainActivity.class));
                }
            }
        };
        splash_time.start();
    }
}
