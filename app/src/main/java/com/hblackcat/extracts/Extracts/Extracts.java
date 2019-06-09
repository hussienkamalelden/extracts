package com.hblackcat.extracts.Extracts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hblackcat.extracts.R;

public class Extracts extends Activity {

    private Typeface typeFace;
    private Button new_extract,exist_extracts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extracts);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        //Initialize ..
        typeFace = Typeface.createFromAsset(getAssets(), "hamah.ttf");
        new_extract = findViewById(R.id.new_extract);
        exist_extracts = findViewById(R.id.exist_extracts);
        new_extract.setTypeface(typeFace);
        exist_extracts.setTypeface(typeFace);

        //New Extract Button ..
        new_extract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                try
                {
                    Intent i = new Intent("com.hblackcat.extracts.Extracts.NewExtract");
                    startActivity(i);
                }catch(Exception e){
                    Toast.makeText(Extracts.this, getResources().getString(R.string.error) + "" , Toast.LENGTH_SHORT).show();}
            }
        });

        //Exist Extract Button ..
        exist_extracts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                try
                {
                    Intent i = new Intent("com.hblackcat.extracts.Extracts.ExistExtracts");
                    startActivity(i);
                }catch(Exception e){
                    Toast.makeText(Extracts.this, getResources().getString(R.string.error) + "" , Toast.LENGTH_SHORT).show();}
            }
        });
    }

    //Check onBack Pressed ..
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
