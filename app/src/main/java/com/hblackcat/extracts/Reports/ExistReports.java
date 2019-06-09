package com.hblackcat.extracts.Reports;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hblackcat.extracts.ExistRecyclerView.Adapter;
import com.hblackcat.extracts.ExistRecyclerView.ExistDataCatcher;
import com.hblackcat.extracts.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExistReports extends Activity {

    private Typeface typeFace;
    private List<ExistDataCatcher> catcher_arr;
    private Context context =this;
    private RecyclerView recycler_view;
    private Adapter custom_adapter;
    private TextView no_files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exists);
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);

        //My CODE..
        typeFace = Typeface.createFromAsset(getAssets(), "hamah.ttf");
        catcher_arr = new ArrayList<>(); // array of objects to received data ..
        no_files = findViewById(R.id.no_files);
        recycler_view = findViewById(R.id.recycler_view);
        no_files.setTypeface(typeFace);
        no_files.setVisibility(View.GONE);

        //set up Adapter with recyclerView ..
        custom_adapter =new Adapter(context,catcher_arr,"Reports"); // send context in parameter ..
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        recycler_view.setAdapter(custom_adapter);

        //get files from directory and update RecyclerView ..
        try{
            updateRecyclerView();
        }catch (Exception e){e.printStackTrace();}
    }

    //get files from directory and update RecyclerView ..
    public void updateRecyclerView() {
        try {
            File f = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/Reports");
            f.mkdirs();
            File[] files = f.listFiles();
            for (int i = 0; i < files.length; i++)
                if (!catcher_arr.contains(files[i].getName()))
                    catcher_arr.add(new ExistDataCatcher(files[i].getName()));
            custom_adapter.notifyDataSetChanged(); //Refresh recyclerView to get all new info from arraylist (DataCatcher) ..

            //check if there files or not ..
            if (catcher_arr.size() == 0) {
                recycler_view.setVisibility(View.GONE);
                no_files.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){e.printStackTrace();}
    }

    //Check onBack Pressed ..
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
    }

    //onDestroy ..
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            // clear ArrayList in case it run before .. clear better and faster than removeAll() method ..
            catcher_arr.clear();
        }catch (Exception e){e.printStackTrace();}
    }
}
