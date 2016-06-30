package com.paditech.cvmarker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.paditech.cvmarker.R;
import com.paditech.cvmarker.pdf.AllTemplate;
import com.paditech.cvmarker.utils.FileUtil;

/**
 * Created by USER on 27/6/2016.
 */
public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                LoadFileAsync async = new LoadFileAsync();
                async.doInBackground(null);
                Intent mainIntent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private class LoadFileAsync extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            String simple = FileUtil.getInternalFilePath(SplashActivity.this);
            AllTemplate simpleClean;
            simpleClean = new AllTemplate(SplashActivity.this);
            simpleClean.setContent(simple);

            return null;
        }

    }
}
