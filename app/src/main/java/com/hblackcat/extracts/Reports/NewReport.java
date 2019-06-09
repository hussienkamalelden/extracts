package com.hblackcat.extracts.Reports;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.hblackcat.extracts.DataBase.MySQLiteHelper;
import com.hblackcat.extracts.HTML_WebViewer.ReportsHtmlDesign;
import com.hblackcat.extracts.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class NewReport extends AppCompatActivity {

    private MySQLiteHelper db;
    private Context context = this;
    private Toolbar toolbar;
    private Button toolbar_done,project_date_button,choose_images;
    private EditText project_title_edit,content_text_edit;
    private int mYear,mMonth,mDay;
    private List<String> path;
    private String the_date = " ";
    private int counter = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_report);
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);

        // keep screen on ..
        try{
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } catch (Exception e) {e.printStackTrace();}

        //Initialize ..
        toolbar_done = findViewById(R.id.toolbar_done);
        choose_images = findViewById(R.id.choose_images);
        project_date_button = findViewById(R.id.project_date_button);
        project_title_edit = findViewById(R.id.project_title_edit);
        content_text_edit = findViewById(R.id.content_text_edit);
        db =new MySQLiteHelper(this);
        project_title_edit.requestFocus(); // to make focus in first edittext ..

        //create DataBase ..
        try{
            db.createDataBase(); //create database IF NOT EXIST ..
            db.openDataBase(); //open database ..
        }catch (Exception e){e.printStackTrace();}

        //Create Action bar ..
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Project date Button ..
        project_date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try
                {
                    // To show current date in the datepicker
                    Calendar mcurrentDate = Calendar.getInstance();
                    mYear = mcurrentDate.get(Calendar.YEAR);
                    mMonth = mcurrentDate.get(Calendar.MONTH);
                    mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                    //open DatePickerDialog ..
                    DatePickerDialog mDatePicker = new DatePickerDialog(NewReport.this, new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                            Calendar myCalendar = Calendar.getInstance();
                            myCalendar.set(Calendar.YEAR, selectedyear);
                            myCalendar.set(Calendar.MONTH, selectedmonth);
                            myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            project_date_button.setText(sdf.format(myCalendar.getTime()));
                            the_date = sdf.format(myCalendar.getTime())+"";

                            mDay = selectedday;
                            mMonth = selectedmonth;
                            mYear = selectedyear;
                        }
                    }, mYear, mMonth, mDay);
                    mDatePicker.show();
                }catch(Exception e){Toast.makeText(NewReport.this, getResources().getString(R.string.error) + "" , Toast.LENGTH_SHORT).show();}
            }
        });

        //Choose images Button ..
        choose_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try
                {
                    //open custom Gallery ..
                    ArrayList<String> paths = new ArrayList<String>();
                    Intent intent = new Intent(NewReport.this, MultiImageSelectorActivity.class);
                    // whether show camera
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
                    // max select image amount
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 5);
                    // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                    // default select images (support array list)
                    intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, paths);
                    startActivityForResult(intent, 1211);
                }catch(Exception e){Toast.makeText(NewReport.this, getResources().getString(R.string.error) + "" , Toast.LENGTH_SHORT).show();}
            }
        });

        //content text editText onTouch to handle scroll inside scrollView..
        content_text_edit.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                try
                {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }catch(Exception e){e.printStackTrace();}
                return false;
            }
        });

        //Done Button ..
        toolbar_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try
                {
                    //Insert Data in DataBase ..
                    db.insertIntoDBNames("project_title_edit",project_title_edit.getText().toString());
                    db.insertIntoDBNames("the_date",the_date);
                    db.insertIntoDBNames("content_text_edit",content_text_edit.getText().toString());

                    //check path (picked images) is empty or not ..
                    if(path != null) {
                        //not empty ..
                        //Insert Number of Images ..
                        db.insertIntoDBNames("images_number", path.size() + "");

                        //Insert Images paths in DataBase ..
                        for (String image : path) {
                            db.insertIntoDBNames("image_"+counter, image);
                            counter++;
                        }
                    }else {
                        //empty ..
                        //Insert Number of Images ..
                        db.insertIntoDBNames("images_number", "0");
                    }

                    //Create and Generate HTML file ..
                    new ReportsHtmlDesign(context).createFolderAndFile();

                    //finish activity ..
                    finish();
                }catch(Exception e){Toast.makeText(NewReport.this, getResources().getString(R.string.error) + "" , Toast.LENGTH_SHORT).show();}
            }
        });
    }

    //onActivityResult To handle when an images is selected from the custom gallery  ..
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1211){
            if(resultCode == RESULT_OK){
                try {
                    // Get the result list of select image paths
                    path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    choose_images.setText(""+path);
                }catch(Exception e){Toast.makeText(NewReport.this, getResources().getString(R.string.error) + "" , Toast.LENGTH_SHORT).show();}
            }
        }
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
            builder.setMessage("هل تريد الرجوع سيتم مسح البيانات؟")
                    .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //end activity ..
                            finish();
                            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
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

        //Drop db ..
        try
        {
            new MySQLiteHelper(this).dropDataBase();
        }catch (Exception e){e.printStackTrace();}
    }
}
