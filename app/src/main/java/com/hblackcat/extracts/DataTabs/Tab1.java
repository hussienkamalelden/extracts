package com.hblackcat.extracts.DataTabs;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hblackcat.extracts.DataBase.MySQLiteHelper;
import com.hblackcat.extracts.R;

import android.net.Uri;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class Tab1 extends Fragment implements View.OnClickListener {

    private MySQLiteHelper db;
    private int mYear,mMonth,mDay;
    private Button choose_logo,from_button,to_button;
    private ImageView rec_hor,rec_ver,square;
    private EditText extract_num_edit,to_gentleman_edit,operation_edit,about_edit,attachments_edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1,container,false);

        //Initialize ..
        choose_logo = view.findViewById(R.id.choose_logo);
        rec_hor = view.findViewById(R.id.rec_hor);
        rec_ver = view.findViewById(R.id.rec_ver);
        square = view.findViewById(R.id.square);
        extract_num_edit = view.findViewById(R.id.extract_num_edit);
        to_gentleman_edit = view.findViewById(R.id.to_gentleman_edit);
        operation_edit = view.findViewById(R.id.operation_edit);
        about_edit = view.findViewById(R.id.about_edit);
        attachments_edit = view.findViewById(R.id.attachments_edit);
        from_button = view.findViewById(R.id.from_button);
        to_button = view.findViewById(R.id.to_button);
        choose_logo.setOnClickListener(this);
        rec_hor.setOnClickListener(this);
        rec_ver.setOnClickListener(this);
        square.setOnClickListener(this);
        from_button.setOnClickListener(this);
        to_button.setOnClickListener(this);
        db =new MySQLiteHelper(getActivity());

        return view;
    }

    //OnclickListener ..
    @Override
    public void onClick(View v) {
        //do what you want to do when button is clicked ..
        switch (v.getId()) {
            //////////////////////////////// choose_logo ////////////////////////////////
            case R.id.choose_logo:
                // choose logo button ..
                try {
                    // To open up a gallery browser
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Logo"), 1);

                }catch (Exception e){e.printStackTrace();}
                break;
            //////////////////////////////// rec_hor ////////////////////////////////
            case R.id.rec_hor:
                // Rectangle Horizontal button ..
               try {
                   rec_hor.setImageDrawable(getResources().getDrawable(R.drawable.rec_h_on));
                   rec_ver.setImageDrawable(getResources().getDrawable(R.drawable.rec_v_off));
                   square.setImageDrawable(getResources().getDrawable(R.drawable.square_off));
                   db.replaceValue("rec_hor","true");
                   db.replaceValue("rec_ver","false");
                   db.replaceValue("square","false");
               }catch (Exception e){e.printStackTrace();}
                break;
            //////////////////////////////// rec_ver ////////////////////////////////
            case R.id.rec_ver:
                // Rectangle Vertical button .. ..
                try {
                    rec_hor.setImageDrawable(getResources().getDrawable(R.drawable.rec_h_off));
                    rec_ver.setImageDrawable(getResources().getDrawable(R.drawable.rec_v_on));
                    square.setImageDrawable(getResources().getDrawable(R.drawable.square_off));
                    db.replaceValue("rec_hor","false");
                    db.replaceValue("rec_ver","true");
                    db.replaceValue("square","false");
                }catch (Exception e){e.printStackTrace();}
                break;
            //////////////////////////////// square ////////////////////////////////
            case R.id.square:
                // Square button ..
                try {
                    rec_hor.setImageDrawable(getResources().getDrawable(R.drawable.rec_h_off));
                    rec_ver.setImageDrawable(getResources().getDrawable(R.drawable.rec_v_off));
                    square.setImageDrawable(getResources().getDrawable(R.drawable.square_on));
                    db.replaceValue("rec_hor","false");
                    db.replaceValue("rec_ver","false");
                    db.replaceValue("square","true");
                }catch (Exception e){e.printStackTrace();}
                break;
            //////////////////////////////// from_button ////////////////////////////////
            case R.id.from_button:
                // Date from button ..
                try {
                    // To show current date in the datepicker
                    Calendar mcurrentDate = Calendar.getInstance();
                    mYear = mcurrentDate.get(Calendar.YEAR);
                    mMonth = mcurrentDate.get(Calendar.MONTH);
                    mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                    //open DatePickerDialog ..
                    DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                            Calendar myCalendar = Calendar.getInstance();
                            myCalendar.set(Calendar.YEAR, selectedyear);
                            myCalendar.set(Calendar.MONTH, selectedmonth);
                            myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            from_button.setText(sdf.format(myCalendar.getTime()));
                            //set in db ..
                            db.replaceValue("from_date",sdf.format(myCalendar.getTime()).toString());

                            mDay = selectedday;
                            mMonth = selectedmonth;
                            mYear = selectedyear;
                        }
                    }, mYear, mMonth, mDay);
                    mDatePicker.show();
                }catch (Exception e){e.printStackTrace();}
                break;
            //////////////////////////////// to_button ////////////////////////////////
            case R.id.to_button:
                // Date to button ..
                try {
                    // To show current date in the datepicker
                    Calendar mcurrentDate = Calendar.getInstance();
                    mYear = mcurrentDate.get(Calendar.YEAR);
                    mMonth = mcurrentDate.get(Calendar.MONTH);
                    mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                    //open DatePickerDialog ..
                    DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                            Calendar myCalendar = Calendar.getInstance();
                            myCalendar.set(Calendar.YEAR, selectedyear);
                            myCalendar.set(Calendar.MONTH, selectedmonth);
                            myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            to_button.setText(sdf.format(myCalendar.getTime()));
                            //set in db ..
                            db.replaceValue("to_date",sdf.format(myCalendar.getTime()).toString());

                            mDay = selectedday;
                            mMonth = selectedmonth;
                            mYear = selectedyear;
                        }
                    }, mYear, mMonth, mDay);
                    mDatePicker.show();
                }catch (Exception e){e.printStackTrace();}
                break;
        }
    }

    // To handle when an image is selected from the browser, add the following to your Activity ..
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                // get path ..
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex); //path ..
                // set path in database ..
                db.replaceValue("logo_path",picturePath);
                c.close();
                //get pic name ..
                File f = new File(picturePath);
                String imageName = f.getName();
                //set pic name on button ..
                choose_logo.setText(""+imageName);
            }
        }
    }

    //onDestroy .. get data from edittexts and replace them in db ..
    @Override
    public void onDestroy() {
        super.onDestroy();
        try
        {
            db.replaceValue("extract_num_edit",extract_num_edit.getText().toString());
            db.replaceValue("to_gentleman_edit",to_gentleman_edit.getText().toString());
            db.replaceValue("operation_edit",operation_edit.getText().toString());
            db.replaceValue("about_edit",about_edit.getText().toString());
            db.replaceValue("attachments_edit",attachments_edit.getText().toString());
        }catch (Exception e){e.printStackTrace();}
    }
}
