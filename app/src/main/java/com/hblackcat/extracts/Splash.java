package com.hblackcat.extracts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        try
        {
            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                public void run() {
                    try {
                        Intent i = new Intent("com.hblackcat.extracts.MainActivity");
                        startActivity(i);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 2000);
        }catch (Exception e){
            Intent i=new Intent("com.hblackcat.extracts.MainActivity");
            startActivity(i);
            finish();
        }
    }
}