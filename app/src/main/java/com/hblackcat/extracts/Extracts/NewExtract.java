package com.hblackcat.extracts.Extracts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.hblackcat.extracts.DataBase.MySQLiteHelper;
import com.hblackcat.extracts.DataTabs.SectionsPageAdapter;
import com.hblackcat.extracts.DataTabs.Tab1;
import com.hblackcat.extracts.DataTabs.Tab2;
import com.hblackcat.extracts.DataTabs.Tab3;
import com.hblackcat.extracts.DataTabs.Tab3Extra.DialogNewItemTab3;
import com.hblackcat.extracts.DataTabs.Tab4;
import com.hblackcat.extracts.DataTabs.Tab4Extra.DialogNewItemTab4;
import com.hblackcat.extracts.HTML_WebViewer.ExtractsHtmlDesign;
import com.hblackcat.extracts.R;

public class NewExtract extends AppCompatActivity {

    private MySQLiteHelper db;
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private Context context = this;
    private Toolbar toolbar;
    private Button ToolbarDone;
    private boolean done_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_extract);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        // keep screen on ..
        try{
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } catch (Exception e) {e.printStackTrace();}

        //Initialize ..
        ToolbarDone = findViewById(R.id.ToolbarDone);
        db =new MySQLiteHelper(this);

        //create DataBase ..
        try{
            db.createDataBase(); //create database IF NOT EXIST ..
            db.openDataBase(); //open database ..
        }catch (Exception e){e.printStackTrace();}

        //Create Action bar ..
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //call Sections PageAdapter Constructor ..
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        // Set up Tabs with ViewPager ..
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //Done Button ..
        ToolbarDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try
                {
                    //open check dialog ..
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder.setMessage("تأكيد التنفيذ !")
                            .setPositiveButton("استمرار", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    //Make Done button true ..
                                    done_btn =true;

                                    //finish activity ..
                                    finish();
                                }
                            })
                            .setNegativeButton("رجوع", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            }).show();
                }catch(Exception e){
                    Toast.makeText(NewExtract.this, getResources().getString(R.string.error) + "" , Toast.LENGTH_SHORT).show();}
            }
        });
    }

    //setup ViewPager ..
    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1(), "بيانات");
        adapter.addFragment(new Tab2(), "إدارة");
        adapter.addFragment(new Tab3(), "بنود");
        adapter.addFragment(new Tab4(), "خصومات");
        //to make pages kept 'alive' ..
        //****ATTENTION**** if it cause problem in memory .. search of savestate() and use it instead of setOffscreenPageLimit ..
        // or getvalues from database
        viewPager.setOffscreenPageLimit(3); // the number of "off screen" pages to keep loaded each side of the current page ..
        viewPager.setAdapter(adapter);
    }

    //Check onBack Pressed ..
    @Override
    public void onBackPressed() {
        //open check dialog ..
        try {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(context);
            }
            builder.setMessage("هل تريد الرجوع، سيتم مسح البيانات؟")
                    .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //end activity ..
                            finish();
                            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
                        }
                    })
                    .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    }).show();
        }catch (Exception e){e.printStackTrace();}
    }

    //onDestroy ..
    @Override
    protected void onDestroy() {
        super.onDestroy();

        //if Done Button == true ..
        if(done_btn == true)
        {
            try {
                //Make Done button false ..
                done_btn = false;

                //Create and Generate HTML file ..
                new ExtractsHtmlDesign(context).createFolderAndFile();
            }catch (Exception e){e.printStackTrace();}
        }

        //Drop db ..
        try
        {
            new MySQLiteHelper(this).dropDataBase();
        }catch (Exception e){e.printStackTrace();}

        //clear ArrayList ..
        DialogNewItemTab3.catcher.clear(); // clear ArrayList better and faster than removeAll() method ..
        DialogNewItemTab4.catcher_4.clear(); // clear ArrayList better and faster than removeAll() method ..
    }
}