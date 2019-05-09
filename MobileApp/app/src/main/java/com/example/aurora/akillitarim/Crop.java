package com.example.aurora.akillitarim;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Crop {
    public String productName;
    public String cropBegin;
    public String cropEnd;
    public String disinfectionTime;
    public boolean wetland;
    public String note;
    private String cropId;
    public static long idCounter;


    public Crop(int initialId){
        String productName = "noProduct";
        String cropBegin = "";
        String cropEnd = "";
        String disinfectionTime = "";
        boolean wetland = false;
        String note ="";
        String cropId = "0";
        idCounter = initialId;
    }

    public Crop(String productName, String cropBegin, String note)  {
        this.productName = productName;
        this.cropBegin = cropBegin;
        this.note = note;

        setCropId(createID());
/*
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date cropBeginTime = null;
        try {
            cropBeginTime = dateFormat.parse("20190102000000");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Date cropEndTime;
        Calendar c = Calendar.getInstance();
        c.setTime(cropBeginTime);


*/

        if(productName.equals("Domates")){
/*

            c.add(Calendar.DATE, 10);
            cropEndTime = c.getTime();

            Log.d("END TIME  ",cropEndTime.toString());

*/

            setCropEnd("2021");
            setDisinfectionTime("2021");
            setWetland(false);
        }

        if(productName.equals("Kavun")){
 /*           c.add(Calendar.DATE, 7);
            cropEndTime = c.getTime();
            Log.d("END TIME  ",cropEndTime.toString());

            setCropEnd(cropEndTime.toString());
  */
            setCropEnd("2021");
            setDisinfectionTime("2026");
            setWetland(true);
        }


        if(productName.equals("Biber")){
            setCropEnd("2021");
            setDisinfectionTime("2026");
            setWetland(true);
        }
        if(productName.equals("Karpuz")){
 /*           c.add(Calendar.DATE, 65);
            cropEndTime = c.getTime();
            Log.d("END TIME  ",cropEndTime.toString());

            setCropEnd(cropEndTime.toString());
 */
            setCropEnd("2021");
            setDisinfectionTime("2026");
            setWetland(true);
        }

        if(productName.equals("Buğday")){
  /*          c.add(Calendar.DATE, 35);
            cropEndTime = c.getTime();
            Log.d("END TIME  ",cropEndTime.toString());

            setCropEnd(cropEndTime.toString());
   */
            setCropEnd("2021");
            setDisinfectionTime("2026");
            setWetland(true);
        }


        if(productName.equals("Mısır")){
            setCropEnd("2025");
            setDisinfectionTime("2026");
            setWetland(true);
        }

        if(productName.equals("Nohut")){
            setCropEnd("2025");
            setDisinfectionTime("2026");
            setWetland(true);
        }

        if(productName.equals("Ay Çekirdeği")){
            setCropEnd("2025");
            setDisinfectionTime("2026");
            setWetland(true);
        }

        if(productName.equals("Fasulye")){
            setCropEnd("2025");
            setDisinfectionTime("2026");
            setWetland(true);
        }
    }



    public String getCropId() {
        return cropId;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
    }

    public static void initializeId(long initialId) {
        Crop.idCounter = idCounter;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setCropBegin(String cropBegin) {
        this.cropBegin = cropBegin;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setCropEnd(String cropEnd) {
        this.cropEnd = cropEnd;
    }

    public void setDisinfectionTime(String disinfectionTime) {
        this.disinfectionTime = disinfectionTime;
    }

    public void setWetland(boolean wetland) {
        this.wetland = wetland;
    }


    public String getProductName() {
        return productName;
    }

    public String getCropBegin() {
        return cropBegin;
    }

    public String getCropEnd() {
        return cropEnd;
    }

    public String getDisinfectionTime() {
        return disinfectionTime;
    }

    public boolean isWetland() {
        return wetland;
    }

    public String getNote() {
        return note;
    }


    public static synchronized String createID()
    {
        return String.valueOf(idCounter++);
    }
}

