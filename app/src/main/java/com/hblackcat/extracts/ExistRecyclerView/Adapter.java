package com.hblackcat.extracts.ExistRecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.hblackcat.extracts.R;

import java.io.File;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {

    private int counter = 0;
    private int lastPosition= -1;
    private Context context;
    private List<ExistDataCatcher> catcher_arr;
    private String which_folder;

    //get Context and array of data ..
    public Adapter(Context contextX, List<ExistDataCatcher> catcher, String which_folders){
        this.which_folder = which_folders;
        this.context = contextX;
        this.catcher_arr = catcher;
    }

    // create holder ..
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.exists_row,parent,false);
        ViewHolder holder = new ViewHolder(row);
        return holder;
    }

    // counter of items ...
    @Override
    public int getItemCount() {
        //check if there data in arraylist ..
        if(catcher_arr != null)
        {
            counter = catcher_arr .size(); // set array size in counter ..
            return catcher_arr.size(); // get array size ..
        }else {
            return counter;
        }
    }

    // this method called every item added or viewed on screen ..
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        // create new object to receive data from list of objects *** ATTENTION: don't worry onBind will not call untill add item ***
        final ExistDataCatcher obj = catcher_arr.get(position);
        holder.file_name.setText(obj.file_name); // Get file_name from arraylist ..

        //Start animation ..
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(holder.file_name.getContext(), android.R.anim.slide_in_left);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }

        //Click to File Item to open it ..
        holder.container_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open file html
                try {
                    ExistDataCatcher object = catcher_arr.get(position);
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+ which_folder+ "/"+object.file_name);
                    //Toast.makeText(context, ""+file, Toast.LENGTH_SHORT).show();

                    //open file with webViewer
                    Intent i = new Intent("com.hblackcat.extracts.HTML_WebViewer.WebViewer");
                    i.putExtra("file", file);
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
                    builder.setTitle("مسح الملف")
                            .setMessage("سيتم مسح الملف .. هل تريد الاستمرار؟")
                            .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete..
                                    counter--; // delete -1 ..
                                    ExistDataCatcher objects = catcher_arr.get(position);
                                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+ which_folder+ "/"+objects.file_name);
                                    file.delete();
                                    catcher_arr.remove(position); // remove this object from list of objects (ExistDataCatcher) ..
                                    notifyItemRemoved(position); // remove item by position ..
                                    notifyItemRangeChanged(position, counter); // give RecyclerView refresh & new counter & positions ..
                                }
                            })
                            .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            }).show();
                }catch (Exception e){e.printStackTrace();}
            }
        });

        //Click to upload button ..
        holder.share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //share file ..
                try {
                    Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                    ExistDataCatcher ob = catcher_arr.get(position);
                    File fileWithinMyDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+ which_folder+ "/"+ob.file_name);
                    if (fileWithinMyDir.exists()) {
                        intentShareFile.setType("*/*");
                        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+ which_folder+ "/"+ob.file_name));
                        intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Sharing File...");
                        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
                        context.startActivity(Intent.createChooser(intentShareFile, "Share File"));
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }

    //created to clear animation ..
    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }
}