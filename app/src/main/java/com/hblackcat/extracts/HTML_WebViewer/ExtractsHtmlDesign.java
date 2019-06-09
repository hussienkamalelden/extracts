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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

public class ExtractsHtmlDesign {

    private Context context;
    private MySQLiteHelper db;
    private String my_html_string;
    private String image,logo_height,logo_width;
    private String items = " ";
    private String discounts = " ";
    private Double item_prev_curr_sum,item_sum;
    private Double item_percentage = 0.0;
    private Double items_sum = 0.0;
    private Double discount_sum = 0.0;
    private Double total = 0.0;

    public ExtractsHtmlDesign(Context contextX)
    {
        context = contextX;
        db = new MySQLiteHelper(context);

        //check logo image and logo dimensions before html string ..
        imgLogoDimens();

        //check Extract Items number and data..
        checkExtractItems();

        //check Extract Discounts number and data..
        checkExtractDiscounts();

        //Equations ..
        Equations();

        //html string code ..
        my_html_string ="<!doctype html>\n" +
                "<html dir=\"rtl\" lang=\"ar\">\n" +
                "\t<head>\n" +
                "\t\t<meta charset=\"utf-8\">\n" +
                "\t\t<style>\n" +
                "\t\t\tbody{margin:30px;}\n" +
                "\t\t\t#div_title{text-align:center;}\n" +
                "\t\t\t#extract_title{font-size:25px; font-weight:bold;}\n" +
                "\t\t\t.extract_num{font-size:20px;}\n" +
                "\t\t\tth{background:#ccffcc;border: 1px solid #000000;}\n" +
                "\t\t\ttd{text-align:center;border: 1px solid #000000;}\n" +
                "\t\t\t.noBorder{empty-cells: hide;}\n" +
                "\t\t\t.special{font-size:20px; border:0; text-align:left;}\n" +
                "\t\t\t.data{font-size:20px;}\n" +
                "\t\t\ttable{border:0;}\n" +
                "\t\t</style>\n" +
                "\t</head>\n" +
                "\t<body>\n" +
                "\t\t<div id=\"div_logo\">\n" +
                this.image +
                "\t\t</div>\n" +
                "\t\t\n" +
                "\t\t<div id=\"div_title\">\n" +
                "\t\t\t<span id=\"extract_title\">مستخلص مقاولى أعمال من الباطن</span><br>\n" +
                "\t\t\t<span class=\"extract_num\">مستخلص جارى  رقم</span><span class=\"extract_num\"> ("+db.getValue("extract_num_edit")+") </span>\n" +
                "\t\t</div><br>\n" +
                "\t\t\n" +
                "\t\t<div>\n" +
                "\t\t\t<span class=\"data\">إلى السيد / </span><span class=\"data\">"+db.getValue("to_gentleman_edit")+"</span>\n" +
                "\t\t</div>\n" +
                "\t\t<div>\n" +
                "\t\t\t<div style=\"float: right; width: 300px;\">\n" +
                "\t\t\t\t<span class=\"data\">عملية / </span><span class=\"data\">"+db.getValue("operation_edit")+"</span>\n" +
                "\t\t\t</div>\n" +
                "\t\t\t<div style=\"float: right; width: 300px;\">\n" +
                "\t\t\t\t<span class=\"data\">عن / </span><span class=\"data\">"+db.getValue("about_edit")+"</span>\n" +
                "\t\t\t</div>\n" +
                "\t\t</div>\n" +
                "\t\t<br style=\"clear: right;\" />\n" +
                "\t\t<div>\n" +
                "\t\t\t<div style=\"float: right; width: 300px;\">\n" +
                "\t\t\t\t<span class=\"data\">مرفقات / </span><span class=\"data\">"+db.getValue("attachments_edit")+"</span>\n" +
                "\t\t\t</div>\n" +
                "\t\t\t<div style=\"float: right; width: 300px;\">\n" +
                "\t\t\t\t<span class=\"data\">المدة من / </span><span class=\"data\">"+db.getValue("from_date")+"</span>\n" +
                "\t\t\t</div>\n" +
                "\t\t\t<div style=\"float: right; width: 300px;\">\n" +
                "\t\t\t<span class=\"data\">الى / </span><span class=\"data\">"+db.getValue("to_date")+"</span>\n" +
                "\t\t\t</div>\n" +
                "\t\t</div>\n" +
                "\t\t\n" +
                "\t\t<table cellspacing=\"0\" border=\"1\" width=\"100%\">\n" +
                "\t\t\t<thead>\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t\t<th rowspan=\"2\"><h3>بند  رقم</h3></th>\n" +
                "\t\t\t\t\t<th rowspan=\"2\"><h3>بيـــــــــــان الأعمال</h3></th>\n" +
                "\t\t\t\t\t<th rowspan=\"2\"><h3>الوحدة</h3></th>\n" +
                "\t\t\t\t\t<th colspan=\"3\"><h3>الكميـــــــة</h3></th>\n" +
                "\t\t\t\t\t<th colspan=\"1\"><h3>فئة</h3></th>\n" +
                "\t\t\t\t\t<th width=\"10%\" rowspan=\"2\"><h3>نسبة صرف البند</h3></th>\n" +
                "\t\t\t\t\t<th width=\"10%\"  colspan=\"1\"><h3>جملة</h3></th>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t\t<th>السابقة</th>\n" +
                "\t\t\t\t\t<th>الحالية</th>\n" +
                "\t\t\t\t\t<th>الجملة</th>\n" +
                "\t\t\t\t\t<th>جنية</th>\n" +
                "\t\t\t\t\t<th>جنية</th>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t</thead>\n" +
                "\t\t\t<tbody>\n" +
                this.items +
                "\t\t\t\t\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t\t<td class=\"noBorder\" colspan=\"6\"></td>\n" +
                "\t\t\t\t\t<td colspan=\"2\" class=\"special\"><span>المجموع</span></td>\n" +
                "\t\t\t\t\t<td colspan=\"1\">"+separatedFormatter(this.items_sum)+"</td>\n"
                + this.discounts +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t\t<td class=\"noBorder\" colspan=\"5\"></td>\n" +
                "\t\t\t\t\t<td colspan=\"2\" class=\"special\"><span>إجمالى الخصومات</span></td>\n" +
                "\t\t\t\t\t<td colspan=\"1\" class=\"discount\">"+separatedFormatter(this.discount_sum)+"</td>\n" +
                "\t\t\t\t\t<td colspan=\"2\"></td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t\t<td class=\"noBorder\" colspan=\"6\"></td>\n" +
                "\t\t\t\t\t<td colspan=\"2\" class=\"special\"><span>المستحق صرفه</span></td>\n" +
                "\t\t\t\t\t<td colspan=\"1\">"+separatedFormatter(this.total)+"</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t</tbody>\n" +
                "\t\t</table> <br>\n" +
                "\t\t\n" +
                "\t\t<div>\n" +
                "\t\t\t<div style=\"float: right; width: 350px;\">\n" +
                "\t\t\t\t<span class=\"data\">مهندس الحصر / </span><span class=\"data\">"+db.getValue("inventory_eng_edit")+"</span>\n" +
                "\t\t\t</div>\n" +
                "\t\t\t<div style=\"float: right; width: 350px;\">\n" +
                "\t\t\t\t<span class=\"data\">مهندس العمليه / </span><span class=\"data\">"+db.getValue("operation_eng_edit")+"</span>\n" +
                "\t\t\t</div>\n" +
                "\t\t</div>\n" +
                "\t\t<br style=\"clear: right;\" />\n" +
                "\t\t<div>\n" +
                "\t\t\t<div style=\"float: right; width: 350px;\">\n" +
                "\t\t\t\t<span class=\"data\">مهندس الجودة / </span><span class=\"data\">"+db.getValue("quality_eng_edit")+"</span>\n" +
                "\t\t\t</div>\n" +
                "\t\t\t<div style=\"float: right; width: 350px;\">\n" +
                "\t\t\t\t<span class=\"data\">المدير الفنى / </span><span class=\"data\">"+db.getValue("technical_dir_edit")+"</span>\n" +
                "\t\t\t</div>\n" +
                "\t\t</div>\n" +
                "\t\t<br style=\"clear: right;\" />\n" +
                "\t\t<div>\n" +
                "\t\t\t<div style=\"float: right; width: 350px;\">\n" +
                "\t\t\t\t<span class=\"data\">الحســــــابـــات / </span><span class=\"data\">"+db.getValue("accounts_edit")+"</span>\n" +
                "\t\t\t</div>\n" +
                "\t\t\t<div style=\"float: right; width: 350px;\">\n" +
                "\t\t\t\t<span class=\"data\">مدير المشروع / </span><span class=\"data\">"+db.getValue("project_man_edit")+"</span>\n" +
                "\t\t\t</div>\n" +
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
            File dir = new File (root.getAbsolutePath() + "/Extracts");
            if(!dir.exists()) {
                dir.mkdirs();
            }

            // create today file ..
            FileOutputStream fOut = new FileOutputStream(root.getAbsolutePath() + "/Extracts/extract_number_"+ db.getValue("extract_num_edit")+".html");
            OutputStreamWriter osw = new OutputStreamWriter(fOut);

            // Write the string to the file
            osw.write(my_html_string);

            // ensure that everything is really written out and close ..
            osw.flush();
            osw.close();
            Toast.makeText(context, "File Generated Successfully !", Toast.LENGTH_SHORT).show();
        }catch (Exception e){e.printStackTrace();}
    }

    //check logo image and logo dimensions ..
    private void imgLogoDimens() {
        //check logo dimensions ..
        try {
            if (db.getValue("rec_hor").equals("true")) {
                this.logo_height = "50";
                this.logo_width = "100";
            } else if (db.getValue("rec_ver").equals("true")) {
                this.logo_height = "100";
                this.logo_width = "50";
            } else if (db.getValue("square").equals("true")) {
                this.logo_height = "100";
                this.logo_width = "100";
            } else {
                //height = 50 and width =100 (default in case user didn't choose) ..
                this.logo_height = "50";
                this.logo_width = "100";
            }
        } catch (Exception e) {e.printStackTrace();}

        //check logo image ..
        try {
            if (db.getValue("logo_path").equals(".........."))
                this.image = " ";
            else
            {
                File file = new File(db.getValue("logo_path"));
                long fileSizeInBytes = file.length()/1024;
                if(fileSizeInBytes <= 200)
                {
                    //if smaller than 200KB make quality 100 ..
                    //***ATTENTION*** : we will convert image to encrypted code (base64) to merge with html file ..
                    Bitmap bm = BitmapFactory.decodeFile(db.getValue("logo_path"));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                    this.image = "\t\t\t<img src=\"" + "data:image/*;base64,"+ encodedImage + "\" height=\"" + this.logo_height + "\" width=\"" + this.logo_width + "\">\n";
                }else{
                    //if bigger than 200KB make quality 20 ..
                    //***ATTENTION*** : we will convert image to encrypted code (base64) to merge with html file ..
                    Bitmap bm = BitmapFactory.decodeFile(db.getValue("logo_path"));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 20, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                    this.image = "\t\t\t<img src=\"" + "data:image/*;base64,"+ encodedImage + "\" height=\"" + this.logo_height + "\" width=\"" + this.logo_width + "\">\n";
                }
            }
        } catch (Exception e) {e.printStackTrace();}
    }

    //check Extract Items number and data..
    private void checkExtractItems() {
        try {
            if (!db.getValue("items_number").equals("0"))
            {
                int number_of_items = Integer.parseInt(db.getValue("items_number"));

                for (int i = 0; i < number_of_items; i++) {

                    this.item_prev_curr_sum = Double.parseDouble(db.getValue("item_prev_amount_"+i)) + Double.parseDouble(db.getValue("item_curr_amount_"+i)); // total of previous and current
                    this.item_percentage = ( Double.parseDouble(db.getValue("item_percen_"+i)) / 100 ); // calc item percentage ..
                    this.item_sum = (item_prev_curr_sum * Double.parseDouble(db.getValue("item_category_"+i))) * this.item_percentage; // total of current item ((prev + current) * category)* percentage ..
                    this.items_sum= this.items_sum+this.item_sum; // total of all items

                    this.items = this.items + "\t\t\t\t<tr>\n" +
                            "\t\t\t\t\t<td>"+db.getValue("item_number_"+i)+"</td>\n" +
                            "\t\t\t\t\t<td>"+db.getValue("item_title_"+i)+"</td>\n" +
                            "\t\t\t\t\t<td>"+db.getValue("item_unit_"+i)+"</td>\n" +
                            "\t\t\t\t\t<td>"+separatedFormatter(Double.parseDouble(db.getValue("item_prev_amount_"+i)))+"</td>\n" +
                            "\t\t\t\t\t<td>"+separatedFormatter(Double.parseDouble(db.getValue("item_curr_amount_"+i)))+"</td>\n" +
                            "\t\t\t\t\t<td>"+separatedFormatter(this.item_prev_curr_sum)+"</td>\n" + // total ..
                            "\t\t\t\t\t<td>"+separatedFormatter(Double.parseDouble(db.getValue("item_category_"+i)))+"</td>\n" +
                            "\t\t\t\t\t<td>"+db.getValue("item_percen_"+i)+" % "+"</td>\n" +
                            "\t\t\t\t\t<td>"+separatedFormatter(this.item_sum)+"</td>\n" + // total ..
                            "\t\t\t\t</tr>\n";
                }
            }
        } catch (Exception e) {e.printStackTrace();}
    }

    //check Extract Discounts number and data..
    private void checkExtractDiscounts() {
        try {
            if (!db.getValue("discounts_number").equals("0"))
            {
                int number_of_discounts = Integer.parseInt(db.getValue("discounts_number"));

                for (int i = 0; i < number_of_discounts; i++) {
                    if(db.getValue("percentage_checkbox_"+i).equals("true")) {
                        this.discount_sum = ((Double.parseDouble(db.getValue("percentage_edit_" + i)) / 100) * this.items_sum) + this.discount_sum;
                        this.discounts = this.discounts + "\t\t\t\t<tr>\n" +
                                "\t\t\t\t\t<td class=\"noBorder\" colspan=\"4\"></td>\n" +
                                "\t\t\t\t\t<td colspan=\"3\" class=\"special\"><span>"+db.getValue("discount_type_" + i)+"</span></td>\n" +
                                "\t\t\t\t\t<td colspan=\"1\" class=\"discount\">"+separatedFormatter(round(((Double.parseDouble(db.getValue("percentage_edit_" + i)) / 100) * this.items_sum),2))+"</td>\n" +
                                "\t\t\t\t\t<td colspan=\"2\"></td>\n" +
                                "\t\t\t\t</tr>\n" ;
                    }else if (db.getValue("net_coast_checkbox_"+i).equals("true")) {
                        this.discount_sum = Double.parseDouble(db.getValue("net_coast_edit_" + i)) + this.discount_sum;
                        this.discounts = this.discounts + "\t\t\t\t<tr>\n" +
                                "\t\t\t\t\t<td class=\"noBorder\" colspan=\"4\"></td>\n" +
                                "\t\t\t\t\t<td colspan=\"3\" class=\"special\"><span>"+db.getValue("discount_type_" + i)+"</span></td>\n" +
                                "\t\t\t\t\t<td colspan=\"1\" class=\"discount\">"+separatedFormatter(Double.parseDouble(db.getValue("net_coast_edit_" + i)))+"</td>\n" +
                                "\t\t\t\t\t<td colspan=\"2\"></td>\n" +
                                "\t\t\t\t</tr>\n" ;
                    }else{
                        this.discounts = this.discounts + "\t\t\t\t<tr>\n" +
                                "\t\t\t\t\t<td class=\"noBorder\" colspan=\"4\"></td>\n" +
                                "\t\t\t\t\t<td colspan=\"3\" class=\"special\"><span>"+db.getValue("discount_type_" + i)+"</span></td>\n" +
                                "\t\t\t\t\t<td colspan=\"1\" class=\"discount\">"+0+"</td>\n" +
                                "\t\t\t\t\t<td colspan=\"2\"></td>\n" +
                                "\t\t\t\t</tr>\n" ;
                    }
                }
            }
        } catch (Exception e) {e.printStackTrace();}
    }

    //Equations ..
    private void Equations() {
        try {
            // after discount sum ..
            this.total = this.items_sum - this.discount_sum;

            //rounds ..
            this.items_sum = round(this.items_sum, 2);
            this.discount_sum = round(this.discount_sum, 2);
            this.total = round(this.total, 2);
        } catch (Exception e) {e.printStackTrace();}
    }

    // method to get rounds of total ..
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    //separated Formatter to make "," every 3 digits .. and calculate and write full big numbers without currency ..
    private String separatedFormatter(Double amount)
    {
        NumberFormat formatter = NumberFormat.getInstance();
        String finalAmount = formatter.format(amount);
        return finalAmount ;
    }

}