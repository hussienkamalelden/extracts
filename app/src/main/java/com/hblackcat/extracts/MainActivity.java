package com.hblackcat.extracts;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends Activity {

    private Typeface typeFace;
    private Button extracts,reports,reset;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize ..
        typeFace = Typeface.createFromAsset(getAssets(), "hamah.ttf");
        extracts = findViewById(R.id.extracts);
        reports = findViewById(R.id.reports);
        reset = findViewById(R.id.reset);
        extracts.setTypeface(typeFace);
        reports.setTypeface(typeFace);
        reset.setTypeface(typeFace);

        //Check Permissions for 6.0 marshmallow read and write and vibration..
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            try {
                if (
                        checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }catch(Exception e){e.printStackTrace();}
        }

        //Extracts Button ..
        extracts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                try
                {
                    Intent i = new Intent("com.hblackcat.extracts.Extracts.Extracts");
                    startActivity(i);
                }catch(Exception e){Toast.makeText(MainActivity.this, getResources().getString(R.string.error) + "" , Toast.LENGTH_SHORT).show();}

                // start full screen ad ..
                try{
                    if (interstitialAd.isLoaded())
                        interstitialAd.show();
                }catch(Exception e){e.printStackTrace();}
            }
        });

        //Reports Button ..
        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                try
                {
                    Intent i = new Intent("com.hblackcat.extracts.Reports.Reports");
                    startActivity(i);
                }catch(Exception e){Toast.makeText(MainActivity.this, getResources().getString(R.string.error) + "" , Toast.LENGTH_SHORT).show();}

                // start full screen ad ..
                try{
                    if (interstitialAd.isLoaded())
                        interstitialAd.show();
                }catch(Exception e){e.printStackTrace();}
            }
        });

        //reset Button ..
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                try
                {
                    // clearing app data
                    String packageName = getApplicationContext().getPackageName();
                    Runtime runtime = Runtime.getRuntime();
                    runtime.exec("pm clear "+packageName);
                }catch(Exception e){Toast.makeText(MainActivity.this, getResources().getString(R.string.error) + "" , Toast.LENGTH_SHORT).show();} }
        });
    }

    //onResume ..
    @Override
    protected void onResume() {
        super.onResume();

        //here put Your Ad:
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.admob_full));
        //load ad..
        try {
            AdRequest adRequest = new AdRequest.Builder().build();
            interstitialAd.loadAd(adRequest);
        }catch(Exception e){}
    }
}
