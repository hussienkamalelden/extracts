package com.hblackcat.extracts.HTML_WebViewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Toast;

import com.hblackcat.extracts.DataBase.MySQLiteHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class ReportsHtmlDesign {

    private Context context;
    private MySQLiteHelper db;
    private String my_html_string;
    private String images = " ";

    public ReportsHtmlDesign(Context contextX)
    {
        context = contextX;
        db = new MySQLiteHelper(context);

        //check Reports Images number and data..
        checkReportsImages();

        //html string code ..
        my_html_string = "<!doctype html>\n" +
                "<html dir=\"rtl\" lang=\"ar\">\n" +
                "\t<head>\n" +
                "\t\t<meta charset=\"utf-8\">\n" +
                "\t\t<style>\n" +
                "\t\t\tbody{margin:30px; border:solid; padding: 10px; }\n" +
                "\t\t\timg{display: block; margin-left: auto; margin-right: auto; margin:0px 0px 10px 0px;}\n" +
                "\t\t\t.box{border: 1px solid #000000; padding: 10px; margin: 10px;}\n" +
                "\t\t\t.data{font-size:20px;font-weight: bold;}\n" +
                "\t\t\t.data2{font-size:20px;word-wrap:break-word;}\n" +
                "\t\t</style>\n" +
                "\t</head>\n" +
                "\t<body>\n" +
                "\t\t\n" +
                "\t\t<div>\n" +
                "\t\t\t<span class=\"data\">المشروع / </span><span class=\"data2\">"+db.getValue("project_title_edit")+"</span>\n" +
                "\t\t</div>\n" +
                "\t\t\n" +
                "\t\t<br>\n" +
                "\t\t\n" +
                "\t\t<div>\n" +
                "\t\t\t<span class=\"data\">التاريخ / </span><span class=\"data2\">"+db.getValue("the_date")+"</span>\n" +
                "\t\t</div>\n" +
                "\t\t\n" +
                "\t\t<br>\n" +
                "\t\t\n" +
                "\t\t<div>\n" +
                "\t\t\t<span class=\"data\">التقرير / </span>\n" +
                "\t\t</div>\n" +
                "\t\t<div class=\"box\">\n" +
                "\t\t\t<span class=\"data2\">"+db.getValue("content_text_edit")+"</span>\n" +
                "\t\t</div>\n" +
                "\t\t\n" +
                "\t\t<br>\n" +
                "\t\t\n" +
                "\t\t<div>\n" +
                "\t\t\t<span class=\"data\">الصور / </span>\n" +
                "\t\t</div>\n" +
                "\t\t\t\n" +
                "\t\t<div class=\"box\">\n" +
                this.images +
                "\t\t</div>\n" +
                "\t</body>\n" +
                "</html>";
    }

    //Create folder if not exist and create file html ..
    public void createFolderAndFile()
    {
        try {
            // create folder if not exist ..
            File root = android.os.Environment.getExternalStorageDirectory();
            File dir = new File (root.getAbsolutePath() + "/Reports");
            if(!dir.exists()) {
                dir.mkdirs();
            }

            // create today file ..
            FileOutputStream fOut = new FileOutputStream(root.getAbsolutePath() + "/Reports/"+ db.getValue("the_date")+".html");
            OutputStreamWriter osw = new OutputStreamWriter(fOut);

            // Write the string to the file
            osw.write(my_html_string);

            // ensure that everything is really written out and close ..
            osw.flush();
            osw.close();
            Toast.makeText(context, "File Generated Successfully !", Toast.LENGTH_SHORT).show();
        }catch (Exception e){e.printStackTrace();}
    }

    //check Reports Images number and data..
    private void checkReportsImages() {
        try {
            if (!db.getValue("images_number").equals("0"))
            {
                int images_number = Integer.parseInt(db.getValue("images_number"));

                for (int i = 0; i < images_number; i++) {
                    checkSize(db.getValue("image_"+i));
                }
            }
        } catch (Exception e) {e.printStackTrace();}
    }

    //check image file if bigger than 200KB or not to change quality ..
    private void checkSize(String path)
    {
        File file = new File(path);
        long fileSizeInBytes = file.length()/1024;
        if(fileSizeInBytes <= 200)
        {
            //if smaller than 200KB make quality 100 ..
            //***ATTENTION*** : we will convert image to encrypted code (base64) to merge with html file ..
            Bitmap bm = BitmapFactory.decodeFile(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

            this.images = this.images + "\t\t\t<img src=\""+  "data:image/*;base64,"+ encodedImage +"\" alt=\"IMAGE\" width=\"100%\" height=\"100%\">\n";
        }else{
            //if bigger than 200KB make quality 20 ..
            //***ATTENTION*** : we will convert image to encrypted code (base64) to merge with html file ..
            Bitmap bm = BitmapFactory.decodeFile(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 20, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

            this.images = this.images + "\t\t\t<img src=\""+  "data:image/*;base64,"+ encodedImage +"\" alt=\"IMAGE\" width=\"100%\" height=\"100%\">\n";
        }
    }
}