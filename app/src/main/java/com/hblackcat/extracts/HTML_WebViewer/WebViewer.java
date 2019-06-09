package com.hblackcat.extracts.HTML_WebViewer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.hblackcat.extracts.R;

import java.io.File;

public class WebViewer extends Activity {

    private WebView web_view;
    private ImageView print;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_viewer);

        //Initialize ..
        print = findViewById(R.id.print);
        web_view = findViewById(R.id.web_view);

        //check mobile version if start from LOLLIPOP or not to show print button ..
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
            print.setVisibility(View.VISIBLE);
        else
            print.setVisibility(View.INVISIBLE);

        //get data from intent and call openFileHTML ..
        File f =(File) getIntent().getExtras().get("file");
        openFileHTML(f);

        //print Button ..
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try
                {
                    createWebPrintJob(web_view);
                }catch(Exception e){Toast.makeText(WebViewer.this, getResources().getString(R.string.error) + "" , Toast.LENGTH_SHORT).show();} }
        });
    }

    //open file come from recyclerview ..
    public void openFileHTML(File file)
    {
        try
        {
            //make view Wide and control it with two fingers ..
            web_view.getSettings().setLoadWithOverviewMode(true);
            web_view.getSettings().setUseWideViewPort(true);
            web_view.getSettings().setBuiltInZoomControls(true);
            web_view.getSettings().setDisplayZoomControls(false);
            web_view.loadUrl("file:///" + file);
        }catch (Exception e){}
    }

    //create PrintJob to print html file through wifi ONLY..
    //device version must be start from LOLLIPOP ..
    private void createWebPrintJob(WebView webView) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
                PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter("MyDocument");
                printManager.print(" My Print Job", printAdapter, new PrintAttributes.Builder().build());
            }
        }catch (Exception e){e.printStackTrace();}
    }
}
