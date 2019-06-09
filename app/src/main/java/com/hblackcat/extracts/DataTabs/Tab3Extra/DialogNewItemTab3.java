package com.hblackcat.extracts.DataTabs.Tab3Extra;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hblackcat.extracts.R;

import java.util.ArrayList;
import java.util.List;

public class DialogNewItemTab3 extends Activity {

    Button Done;
    EditText ItemNumberEditText,BoxEditTex_1,BoxEditTex_2,PreviousAmount,CurrentAmount,BoxEditTex_4,BoxEditTex_5;
    private Intent intent;
    public static List<DataCatcher> catcher = new ArrayList<>(); // array of objects to received data ..
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        //Initialize ..
        Done = findViewById(R.id.Done);
        ItemNumberEditText = findViewById(R.id.ItemNumberEditText);
        BoxEditTex_1 = findViewById(R.id.BoxEditTex_1);
        BoxEditTex_2 = findViewById(R.id.BoxEditTex_2);
        PreviousAmount = findViewById(R.id.PreviousAmount);
        CurrentAmount = findViewById(R.id.CurrentAmount);
        BoxEditTex_4 = findViewById(R.id.BoxEditTex_4);
        BoxEditTex_5 = findViewById(R.id.BoxEditTex_5);
        intent = getIntent();

        //if user click on edit button .. put data in dialog ..
        // hasExtra to check if intent had extra values ..
        if(intent.hasExtra("edit")){
            position = getIntent().getIntExtra("position",0);
            ItemNumberEditText.setText(DialogNewItemTab3.catcher.get(position).itemNumber.toString());
            BoxEditTex_1.setText(DialogNewItemTab3.catcher.get(position).itemTitle.toString());
            BoxEditTex_2.setText(DialogNewItemTab3.catcher.get(position).unit.toString());
            PreviousAmount.setText(DialogNewItemTab3.catcher.get(position).prevAmount.toString());
            CurrentAmount.setText(DialogNewItemTab3.catcher.get(position).currAmount.toString());
            BoxEditTex_4.setText(DialogNewItemTab3.catcher.get(position).category.toString());
            BoxEditTex_5.setText(DialogNewItemTab3.catcher.get(position).percen.toString());
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
                        DataCatcher DataObje = new DataCatcher(ItemNumberEditText.getText().toString(),
                            BoxEditTex_1.getText().toString(), BoxEditTex_2.getText().toString(),
                            PreviousAmount.getText().toString(), CurrentAmount.getText().toString(),
                            BoxEditTex_4.getText().toString(),BoxEditTex_5.getText().toString());
                        DialogNewItemTab3.catcher.set(position,DataObje);
                        Toast.makeText(DialogNewItemTab3.this, getResources().getString(R.string.item_edited) + "", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                    //if Done for First Time ..
                        DataCatcher DataObj = new DataCatcher(ItemNumberEditText.getText().toString(),
                                BoxEditTex_1.getText().toString(), BoxEditTex_2.getText().toString(),
                                PreviousAmount.getText().toString(), CurrentAmount.getText().toString(),
                                BoxEditTex_4.getText().toString(),BoxEditTex_5.getText().toString());
                        DialogNewItemTab3.catcher.add(DataObj);
                        Toast.makeText(DialogNewItemTab3.this, getResources().getString(R.string.item_added) + "", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }
}
