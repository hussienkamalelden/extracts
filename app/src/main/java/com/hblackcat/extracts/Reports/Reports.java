package com.hblackcat.extracts.Reports;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hblackcat.extracts.R;

public class Reports extends Activity {

    private Typeface typeFace;
    private Button new_report,exist_reports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports);
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);

        //Initialize ..
        typeFace = Typeface.createFromAsset(getAssets(), "hamah.ttf");
        new_report = findViewById(R.id.new_report);
        exist_reports = findViewById(R.id.exist_reports);
        new_report.setTypeface(typeFace);
        exist_reports.setTypeface(typeFace);

        //New Report Button ..
        new_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                try
                {
                    Intent i = new Intent("com.hblackcat.extracts.Reports.NewReport");
                    startActivity(i);
                }catch(Exception e){
                    Toast.makeText(Reports.this, getResources().getString(R.string.error) + "" , Toast.LENGTH_SHORT).show();}
            }
        });

        //Exist Reports Button ..
        exist_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                try
                {
                    Intent i = new Intent("com.hblackcat.extracts.Reports.ExistReports");
                    startActivity(i);
                }catch(Exception e){
                    Toast.makeText(Reports.this, getResources().getString(R.string.error) + "" , Toast.LENGTH_SHORT).show();}
            }
        });
    }

    //Check onBack Pressed ..
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
    }
}
