package com.hblackcat.extracts.DataTabs.Tab4Extra;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.hblackcat.extracts.R;

import java.util.ArrayList;
import java.util.List;

public class DialogNewItemTab4 extends Activity {

    private Button Done;
    private EditText discount_type_edit,percentage_edit,net_coast_edit;
    private CheckBox percentage_checkbox,net_coast_checkbox;
    private TextView percentage_note,net_coast_note;
    private RelativeLayout percentage_box,net_coast_box;
    private Intent intent;
    public static List<DataCatcher_4> catcher_4 = new ArrayList<>(); // array of objects to received data ..
    private int position;
    private String percentage_bool = "false",net_coast_bool = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_tab4);

        //Initialize ..
        Done = findViewById(R.id.Done);
        discount_type_edit = findViewById(R.id.discount_type_edit);
        percentage_edit = findViewById(R.id.percentage_edit);
        net_coast_edit = findViewById(R.id.net_coast_edit);
        percentage_checkbox = findViewById(R.id.percentage_checkbox);
        net_coast_checkbox = findViewById(R.id.net_coast_checkbox);
        percentage_box = findViewById(R.id.percentage_box);
        net_coast_box = findViewById(R.id.net_coast_box);
        percentage_note = findViewById(R.id.percentage_note);
        net_coast_note = findViewById(R.id.net_coast_note);
        intent = getIntent();

        //hide and show elements ..
        try{
            percentage_note.setVisibility(View.GONE);
            net_coast_note.setVisibility(View.GONE);
            percentage_box.setVisibility(View.GONE);
            net_coast_box.setVisibility(View.GONE);
        }catch (Exception e){e.printStackTrace();}

        //if user click on edit button .. put data in dialog ..
        // hasExtra to check if intent had extra values ..
        if(intent.hasExtra("edit")){
            position = getIntent().getIntExtra("position",0);
            discount_type_edit.setText(DialogNewItemTab4.catcher_4.get(position).discount_type_edit.toString());
            if(DialogNewItemTab4.catcher_4.get(position).percentage_checkbox.toString().equals("true"))
            {
                percentageChecked();
                percentage_checkbox.setChecked(true);
            }else if(DialogNewItemTab4.catcher_4.get(position).net_coast_checkbox.toString().equals("true"))
            {
                netCoastChecked();
                net_coast_checkbox.setChecked(true);
            }
            percentage_edit.setText(DialogNewItemTab4.catcher_4.get(position).percentage_edit.toString());
            net_coast_edit.setText(DialogNewItemTab4.catcher_4.get(position).net_coast_edit.toString());
        }

        //Done Button ..
        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    //if Done for Edit ..
                    if(intent.hasExtra("edit"))
                    {
                        if(percentage_bool.equals("false") && net_coast_bool.equals("false"))
                        {
                            DataCatcher_4 DataObje = new DataCatcher_4(discount_type_edit.getText().toString(),
                                    percentage_bool, net_coast_bool,
                                    "0", "0");
                            DialogNewItemTab4.catcher_4.set(position,DataObje);
                        }else {
                            DataCatcher_4 DataObje = new DataCatcher_4(discount_type_edit.getText().toString(),
                                    percentage_bool, net_coast_bool,
                                    percentage_edit.getText().toString(), net_coast_edit.getText().toString());
                            DialogNewItemTab4.catcher_4.set(position,DataObje);
                        }
                        Toast.makeText(DialogNewItemTab4.this, getResources().getString(R.string.item_edited) + "", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        //if Done for First Time ..
                        DataCatcher_4 DataObj = new DataCatcher_4(discount_type_edit.getText().toString(),
                                percentage_bool, net_coast_bool,
                                percentage_edit.getText().toString(), net_coast_edit.getText().toString());
                        // -1 to make (ماسبق صرفه) last row .. if it make problem in future put it only DialogNewItemTab4.catcher_4.add(DataObj); and remove if statement ..
                        if(DialogNewItemTab4.catcher_4.size() >= 1)
                            DialogNewItemTab4.catcher_4.add(DialogNewItemTab4.catcher_4.size() -1 ,DataObj);
                        else
                            DialogNewItemTab4.catcher_4.add(DataObj);
                        Toast.makeText(DialogNewItemTab4.this, getResources().getString(R.string.item_added) + "", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }

    //checkboxes buttons on click ..
    public void checkboxOnClick(View view) {
        // now check Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        try {
            switch (view.getId()) {
                ////////////////////////   percentage_checkbox   ////////////////////////
                case R.id.percentage_checkbox:
                    if (checked == false)
                        percentageUnchecked();
                    else
                        percentageChecked();
                    break;
                ////////////////////////   net_coast_checkbox   ////////////////////////
                case R.id.net_coast_checkbox:
                    if (checked == false)
                        netCoastUnchecked();
                    else
                        netCoastChecked();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // if percentage_checkbox checked (true) ..
    private void percentageChecked()
    {
        percentage_note.setVisibility(View.VISIBLE);
        net_coast_checkbox.setVisibility(View.GONE);
        net_coast_note.setVisibility(View.GONE);
        percentage_box.setVisibility(View.VISIBLE);
        net_coast_box.setVisibility(View.GONE);
        percentage_bool = "true";
    }

    // if percentage_checkbox UNchecked (false) ..
    private void percentageUnchecked()
    {
        percentage_note.setVisibility(View.GONE);
        net_coast_note.setVisibility(View.GONE);
        percentage_box.setVisibility(View.GONE);
        net_coast_box.setVisibility(View.GONE);
        net_coast_checkbox.setVisibility(View.VISIBLE);
        percentage_bool = "false";
    }

    // if net_coast_checkbox checked (true) ..
    private void netCoastChecked()
    {
        percentage_note.setVisibility(View.GONE);
        percentage_checkbox.setVisibility(View.GONE);
        net_coast_note.setVisibility(View.VISIBLE);
        percentage_box.setVisibility(View.GONE);
        net_coast_box.setVisibility(View.VISIBLE);
        net_coast_bool = "true";
    }

    // if net_coast_checkbox UNchecked (false) ..
    private void netCoastUnchecked()
    {
        percentage_note.setVisibility(View.GONE);
        net_coast_note.setVisibility(View.GONE);
        percentage_box.setVisibility(View.GONE);
        net_coast_box.setVisibility(View.GONE);
        percentage_checkbox.setVisibility(View.VISIBLE);
        net_coast_bool = "false";
    }
}
