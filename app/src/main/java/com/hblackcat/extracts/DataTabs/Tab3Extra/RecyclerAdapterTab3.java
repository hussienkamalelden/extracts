package com.hblackcat.extracts.DataTabs.Tab3Extra;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.hblackcat.extracts.R;

public class RecyclerAdapterTab3 extends RecyclerView.Adapter<ViewHolderTab3> {

    public int counter = 0;
    private int lastPosition= -1;
    private Context context;

    //get Context ..
    public RecyclerAdapterTab3(Context contextX){
        this.context=contextX;
    }

    // create holder ..
    @Override
    public ViewHolderTab3 onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab3_row,parent,false);
        ViewHolderTab3 holder = new ViewHolderTab3(row);
        return holder;
    }

    // counter of items ...
    @Override
    public int getItemCount() {
        //check if there data in arraylist ..
        if(DialogNewItemTab3.catcher != null)
        {
            counter = DialogNewItemTab3.catcher .size(); // set array size in counter ..
            setTextOfParentView(); // setText Of Parent View ..
            return DialogNewItemTab3.catcher .size(); // get array size ..
        }else {
            return counter;
        }
    }

    // this method called every item added or viewed on screen ..
    @Override
    public void onBindViewHolder(ViewHolderTab3 holder, final int position) {

        // create new object to receive data from list of objects *** ATTENTION: don't worry onBind will not call untill add item ***
        DataCatcher obj = DialogNewItemTab3.catcher.get(position);
        holder.ItemNumber.setText(obj.itemNumber); // Get Item Number from arraylist ..
        holder.ItemTitle.setText(obj.itemTitle); // Get Item Title from arraylist ..

        //Start animation ..
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(holder.ItemNumber.getContext(), android.R.anim.slide_in_left);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }

        //ContainerBox to edit dialog ..
        holder.ContainerBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Delete item ..
                try {
                    DataCatcher objec = DialogNewItemTab3.catcher.get(position);
                    Intent i = new Intent("com.hblackcat.extracts.DataTabs.Tab3Extra.DialogNewItemTab3");
                    i.putExtra("edit","edit");
                    i.putExtra("position",position);
                    context.startActivity(i);
                }catch (Exception e){e.printStackTrace();}
            }
        });

        //Click to Delete Item ..
        holder.DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Delete item ..
                try {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder.setTitle("مسح البند")
                            .setMessage("سيتم مسح البند .. هل تريد الاستمرار؟")
                            .setPositiveButton("استمرار", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete..
                                    counter--; // delete -1 ..
                                    DialogNewItemTab3.catcher.remove(position); // remove this object from list of objects (DataCatcher) ..
                                    setTextOfParentView(); // setText Of Parent View ..
                                    notifyItemRemoved(position); // remove item by position ..
                                    notifyItemRangeChanged(position, counter); // give RecyclerView refresh & new counter & positions ..
                                    Toast.makeText(context, context.getResources().getString(R.string.item_deleted) + "", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("رجوع", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            }).show();
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }

    //created to clear animation ..
    @Override
    public void onViewDetachedFromWindow(ViewHolderTab3 holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    // setText Of Parent View ..
    public void setTextOfParentView()
    {
        TextView ItemsCounter =((Activity)context).findViewById(R.id.ItemsCounter); // inisialize parent textview by context ..
        ItemsCounter.setText(counter + ""); // settext new Value ..
    }
}