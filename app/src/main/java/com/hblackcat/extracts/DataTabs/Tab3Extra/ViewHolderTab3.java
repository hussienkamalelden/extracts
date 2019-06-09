package com.hblackcat.extracts.DataTabs.Tab3Extra;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hblackcat.extracts.R;

public class ViewHolderTab3 extends RecyclerView.ViewHolder {

    RelativeLayout ContainerBox;
    TextView ItemNumber,ItemTitle;
    Button DeleteBtn;

    ViewHolderTab3(View itemView)
    {
        super(itemView);
        ContainerBox = itemView.findViewById(R.id.ContainerBox);
        ItemNumber = itemView.findViewById(R.id.ItemNumber);
        ItemTitle = itemView.findViewById(R.id.ItemTitle);
        DeleteBtn = itemView.findViewById(R.id.DeleteBtn);
    }
}