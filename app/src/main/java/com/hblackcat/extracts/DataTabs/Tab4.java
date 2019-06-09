package com.hblackcat.extracts.DataTabs;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hblackcat.extracts.DataBase.MySQLiteHelper;
import com.hblackcat.extracts.DataTabs.Tab4Extra.RecyclerAdapterTab4;
import com.hblackcat.extracts.R;

import static com.hblackcat.extracts.DataTabs.Tab4Extra.DialogNewItemTab4.catcher_4;

public class Tab4 extends Fragment {

    private MySQLiteHelper db;
    private Button add_discounts;
    private RecyclerView recyclerView_4;
    private TextView items_counter;
    private RecyclerAdapterTab4 custom_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab4,container,false);

        //Initialize ..
        recyclerView_4 = view.findViewById(R.id.recyclerView_4);
        add_discounts = view.findViewById(R.id.add_discounts);
        items_counter = view.findViewById(R.id.items_counter);
        db =new MySQLiteHelper(getActivity());

        //set up customAdapter Inside recyclerView ..
        custom_adapter =new RecyclerAdapterTab4(getActivity()); // send context in parameter ..
        recyclerView_4.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_4.setAdapter(custom_adapter);

        //Button to Add Row ..
        add_discounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open dialog ..
                Intent i = new Intent("com.hblackcat.extracts.DataTabs.Tab4Extra.DialogNewItemTab4");
                startActivity(i);
            }
        });
        return view;
    }

    //onResume ..
    @Override
    public void onResume() {
        super.onResume();
        // refresh customAdapter every time click back or done from dialog ..
        custom_adapter.notifyDataSetChanged(); //Refresh recyclerView ..
        items_counter.setText(custom_adapter.counter_4 + "");
    }

    // we insert data in onDestroy because name column in db is unique .. so insert values only one time
    //onDestroy .. get data from arraylist and insert them into db ..
    @Override
    public void onDestroy() {
        super.onDestroy();

        try
        {
            //first insert number of items in arraylist into db ..
            db.insertIntoDBNames("discounts_number" , catcher_4.size()+"");

            //then insert items content into db ..
            for(int i = 0 ; i < catcher_4 .size() ; i++) {
                db.insertIntoDBNames("discount_type_"+i , catcher_4.get(i).discount_type_edit.toString());
                db.insertIntoDBNames("percentage_checkbox_"+i , catcher_4.get(i).percentage_checkbox.toString());
                db.insertIntoDBNames("net_coast_checkbox_"+i , catcher_4.get(i).net_coast_checkbox.toString());

                if(catcher_4.get(i).percentage_edit.toString().matches(""))
                    db.insertIntoDBNames("percentage_edit_"+i , "0");
                else
                    db.insertIntoDBNames("percentage_edit_"+i , catcher_4.get(i).percentage_edit.toString());

                if(catcher_4.get(i).net_coast_edit.toString().matches(""))
                    db.insertIntoDBNames("net_coast_edit_"+i , "0");
                else
                    db.insertIntoDBNames("net_coast_edit_"+i , catcher_4.get(i).net_coast_edit.toString());
            }
        }catch (Exception e){e.printStackTrace();}
    }
}
