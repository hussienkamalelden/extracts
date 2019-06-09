package com.hblackcat.extracts.DataTabs.Tab4Extra;

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

public class RecyclerAdapterTab4 extends RecyclerView.Adapter<ViewHolderTab4> {

    public int counter_4 = 0;
    private int lastPosition= -1;
    private Context context;
    private String discounts_fixed[] = {"حجز" , "تأمينات اجتماعية" , "مسحوبات من المخازن" , "جارى مصلحة الضرائب" , "ما سبق صرفه حتى المستخلص رقم ()"};

    //get Context ..
    public RecyclerAdapterTab4(Context contextX){
        this.context=contextX;

        for (int i =0 ; i < discounts_fixed.length ; i++) {
            DataCatcher_4 data_obje = new DataCatcher_4(discounts_fixed[i],
                    "false", "false",
                    "0", "0");
            DialogNewItemTab4.catcher_4.add(i, data_obje);
        }
    }

    // create holder ..
    @Override
    public ViewHolderTab4 onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab4_row,parent,false);
        ViewHolderTab4 holder = new ViewHolderTab4(row);
        return holder;
    }

    // counter of items ...
    @Override
    public int getItemCount() {
        //check if there data in arraylist ..
        if(DialogNewItemTab4.catcher_4 != null)
        {
            counter_4 = DialogNewItemTab4.catcher_4 .size(); // set array size in counter ..
            setTextOfParentView(); // setText Of Parent View ..
            return DialogNewItemTab4.catcher_4 .size(); // get array size ..
        }else {
            return counter_4;
        }
    }

    // this method called every item added or viewed on screen ..
    @Override
    public void onBindViewHolder(ViewHolderTab4 holder, final int position) {

        // create new object to receive data from list of objects *** ATTENTION: don't worry onBind will not call untill add item ***
        DataCatcher_4 obj = DialogNewItemTab4.catcher_4.get(position);
        if(obj.percentage_checkbox.equals("true"))
            holder.item_number.setText(" % " + obj.percentage_edit); // Get percentage from arraylist ..
        else if(obj.net_coast_checkbox.equals("true"))
            holder.item_number.setText(obj.net_coast_edit); // Get net_coast from arraylist ..
        else
            holder.item_number.setText("0"); // if all false set value == 0 ..
        holder.item_title.setText(obj.discount_type_edit); // Get Item Title from arraylist ..

        //Start animation ..
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(holder.item_number.getContext(), android.R.anim.slide_in_left);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }

        //ContainerBox to edit dialog ..
        holder.container_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Delete item ..
                try {
                    DataCatcher_4 objec = DialogNewItemTab4.catcher_4.get(position);
                    Intent i = new Intent("com.hblackcat.extracts.DataTabs.Tab4Extra.DialogNewItemTab4");
                    i.putExtra("edit","edit");
                    i.putExtra("position",position);
                    context.startActivity(i);
                }catch (Exception e){e.printStackTrace();}
            }
        });

        //Click to Delete Item ..
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
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
                    builder.setTitle("مسح الخصم")
                            .setMessage("سيتم مسح الخصم .. هل تريد الاستمرار؟")
                            .setPositiveButton("استمرار", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete..
                                    counter_4--; // delete -1 ..
                                    DialogNewItemTab4.catcher_4.remove(position); // remove this object from list of objects (DataCatcher_4) ..
                                    setTextOfParentView(); // setText Of Parent View ..
                                    notifyItemRemoved(position); // remove item by position ..
                                    notifyItemRangeChanged(position, counter_4); // give RecyclerView refresh & new counter & positions ..
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
    public void onViewDetachedFromWindow(ViewHolderTab4 holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    // setText Of Parent View ..
    public void setTextOfParentView()
    {
        TextView ItemsCounter =((Activity)context).findViewById(R.id.items_counter); // inisialize parent textview by context ..
        ItemsCounter.setText(counter_4 + ""); // settext new Value ..
    }
}