package com.hblackcat.extracts.DataTabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.hblackcat.extracts.DataBase.MySQLiteHelper;
import com.hblackcat.extracts.R;

public class Tab2 extends Fragment {

    private MySQLiteHelper db;
    private EditText inventory_eng_edit,operation_eng_edit,quality_eng_edit,technical_dir_edit,accounts_edit,project_man_edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2,container,false);

        //Initialize ..
        inventory_eng_edit = view.findViewById(R.id.inventory_eng_edit);
        operation_eng_edit = view.findViewById(R.id.operation_eng_edit);
        quality_eng_edit = view.findViewById(R.id.quality_eng_edit);
        technical_dir_edit = view.findViewById(R.id.technical_dir_edit);
        accounts_edit = view.findViewById(R.id.accounts_edit);
        project_man_edit = view.findViewById(R.id.project_man_edit);
        db =new MySQLiteHelper(getActivity());

        return view;
    }

    //onDestroy .. get data from edittexts and replace them in db ..
    @Override
    public void onDestroy() {
        super.onDestroy();
        try
        {
            db.replaceValue("inventory_eng_edit",inventory_eng_edit.getText().toString());
            db.replaceValue("operation_eng_edit",operation_eng_edit.getText().toString());
            db.replaceValue("quality_eng_edit",quality_eng_edit.getText().toString());
            db.replaceValue("technical_dir_edit",technical_dir_edit.getText().toString());
            db.replaceValue("accounts_edit",accounts_edit.getText().toString());
            db.replaceValue("project_man_edit",project_man_edit.getText().toString());
        }catch (Exception e){e.printStackTrace();}
    }
}

