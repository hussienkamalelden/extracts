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
import com.hblackcat.extracts.DataTabs.Tab3Extra.DataCatcher;
import com.hblackcat.extracts.DataTabs.Tab3Extra.RecyclerAdapterTab3;
import com.hblackcat.extracts.R;

import java.util.Collections;
import java.util.Comparator;

import static com.hblackcat.extracts.DataTabs.Tab3Extra.DialogNewItemTab3.catcher;

public class Tab3 extends Fragment {

    private MySQLiteHelper db;
    private Button AddItem;
    private RecyclerView recyclerView;
    private TextView ItemsCounter;
    private RecyclerAdapterTab3 customAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab3,container,false);

        //Initialize ..
        recyclerView = view.findViewById(R.id.recyclerView);
        AddItem = view.findViewById(R.id.AddItem);
        ItemsCounter = view.findViewById(R.id.ItemsCounter);
        db =new MySQLiteHelper(getActivity());

        //set up customAdapter Inside recyclerView ..
        customAdapter =new RecyclerAdapterTab3(getActivity()); // send context in parameter ..
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(customAdapter);

        //Button to Add Row ..
        AddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open dialog ..
                Intent i = new Intent("com.hblackcat.extracts.DataTabs.Tab3Extra.DialogNewItemTab3");
                startActivity(i);
            }
        });
        return view;
    }

    //onResume ..
    @Override
    public void onResume() {
        super.onResume();

        //list sort Ascending by item number ..
        try {
            Collections.sort(catcher, new Comparator<DataCatcher>() {
                @Override
                public int compare(DataCatcher lhs, DataCatcher rhs) {
                    return Integer.parseInt(lhs.itemNumber) - Integer.parseInt((rhs.itemNumber));
                }
            });
        }catch (Exception e){e.printStackTrace();}

        // refresh customAdapter every time click back or done from dialog ..
        customAdapter.notifyDataSetChanged(); //Refresh recyclerView ..
        ItemsCounter.setText(customAdapter.counter + "");
    }

    // we insert data in onDestroy because name column in db is unique .. so insert values only one time
    //onDestroy .. get data from arraylist and insert them into db ..
    @Override
    public void onDestroy() {
        super.onDestroy();
        try
        {
            //first insert number of items in arraylist into db ..
            db.insertIntoDBNames("items_number" , catcher.size()+"");

            //then insert items content into db ..
            for(int i = 0 ; i < catcher .size() ; i++) {
                db.insertIntoDBNames("item_number_"+i , catcher.get(i).itemNumber.toString());
                db.insertIntoDBNames("item_title_"+i , catcher.get(i).itemTitle.toString());
                db.insertIntoDBNames("item_unit_"+i , catcher.get(i).unit.toString());

                if(catcher.get(i).prevAmount.toString().matches(""))
                    db.insertIntoDBNames("item_prev_amount_"+i , "0");
                else
                    db.insertIntoDBNames("item_prev_amount_"+i , catcher.get(i).prevAmount.toString());

                if(catcher.get(i).currAmount.toString().matches(""))
                    db.insertIntoDBNames("item_curr_amount_"+i , "0");
                else
                    db.insertIntoDBNames("item_curr_amount_"+i , catcher.get(i).currAmount.toString());

                if(catcher.get(i).category.toString().matches(""))
                    db.insertIntoDBNames("item_category_"+i , "0");
                else
                    db.insertIntoDBNames("item_category_"+i , catcher.get(i).category.toString());

                if(catcher.get(i).percen.toString().matches(""))
                    db.insertIntoDBNames("item_percen_"+i , "100");
                else
                    db.insertIntoDBNames("item_percen_"+i , catcher.get(i).percen.toString());
            }
        }catch (Exception e){e.printStackTrace();}
    }
}
