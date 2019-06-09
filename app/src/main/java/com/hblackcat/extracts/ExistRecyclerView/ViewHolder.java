package com.hblackcat.extracts.ExistRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hblackcat.extracts.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView file_name;
    Button delete_btn,share_btn;
    RelativeLayout container_box;

    ViewHolder(View itemView)
    {
        super(itemView);
        file_name = itemView.findViewById(R.id.file_name);
        container_box = itemView.findViewById(R.id.container_box);
        delete_btn = itemView.findViewById(R.id.delete_btn);
        share_btn = itemView.findViewById(R.id.share_btn);
    }
}
