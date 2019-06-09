package com.hblackcat.extracts.DataTabs.Tab4Extra;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hblackcat.extracts.R;

public class ViewHolderTab4 extends RecyclerView.ViewHolder {

    RelativeLayout container_box;
    TextView item_title,item_number;
    Button delete_btn;

    ViewHolderTab4(View itemView)
    {
        super(itemView);
        container_box = itemView.findViewById(R.id.container_box);
        item_title = itemView.findViewById(R.id.item_title);
        item_number = itemView.findViewById(R.id.item_number);
        delete_btn = itemView.findViewById(R.id.delete_btn);
    }
}