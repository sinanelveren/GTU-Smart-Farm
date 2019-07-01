package com.example.aurora.akillitarim;

import android.util.Log;

import java.io.Serializable;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Crop implements Serializable {
    public String productName;
    public Date cropBegin;
    public Date cropEnd;
    public int ripeningTime;
    public Date disinfectionDate;       //month
    public String timeInfo;
    public boolean wetland;
    public String note;
    private String cropId;
    public static long idCounter;

    private boolean isDisinfection;

    public int disinfectionLeftTime;
    public int ripeningLeftTime;


    final String AYCEKIRDEGI = "Ay Çekirdeği";
    final String BIBER = "Biber";
    final String BUGDAY = "Buğday";
    final String FASULYE = "Fasulye";
    final String DOMATES = "Domates";
    final String KAVUN = "Kavun";
    final String KARPUZ = "Karpuz";
    final String MISIR = "Mısır";
    final String NOHUT = "Nohut";
    final String SARIMSAK = "Sarımsak";



    public Crop(){}

    public Crop(String productName, Date cropBegin, Date cropEnd,
                int ripeningTime, Date disinfectionDate, String timeInfo,
                boolean wetland, String note, String cropId,
                boolean isDisinfection, int disinfectionLeftTime, int ripeningLeftTime) {
        this.productName = productName;
        this.cropBegin = cropBegin;
        this.cropEnd = cropEnd;
        this.ripeningTime = ripeningTime;
        this.disinfectionDate = disinfectionDate;
        this.timeInfo = timeInfo;
        this.wetland = wetland;
        this.note = note;
        this.cropId = cropId;
        this.isDisinfection = isDisinfection;
        this.disinfectionLeftTime = disinfectionLeftTime;
        this.ripeningLeftTime = ripeningLeftTime;
    }

    /**
     * used for set initial id of products
     * @param initialId initial id for products
     */
    public Crop(int initialId){
        idCounter = initialId;
    }

    public Crop(String productName, String cropBegin, String note)  {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date cropBeginTime = null;

        try {
            cropBeginTime = dateFormat.parse(cropBegin);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //Date cropEndTime;
        Calendar c = Calendar.getInstance();
        c.setTime(cropBeginTime);


        this.productName = productName;
        this.cropBegin = cropBeginTime;
        this.timeInfo = cropBegin;
        this.note = note;
        setCropId(createID());



        if(productName.equals(AYCEKIRDEGI)){
            setWetland(false);
            setDisinfection(false);

            this.ripeningTime = 110;
            c.add(Calendar.DATE, 110);
            setCropEnd(c.getTime());

            c.set(2019, 1, 1);
            setDisinfectionDate(c.getTime());
        }

        if(productName.equals(BIBER)){
            setWetland(false);
            setDisinfection(false);

            this.ripeningTime = 20;
            c.add(Calendar.DATE, 20);
            setCropEnd(c.getTime());

            c.set(2019, 2, 1);
            setDisinfectionDate(c.getTime());
        }

        if(productName.equals(BUGDAY)){
            setWetland(false);
            setDisinfection(false);

            this.ripeningTime = 30;
            c.add(Calendar.DATE, 30);
            setCropEnd(c.getTime());

            c.set(2019, 3, 1);
            setDisinfectionDate(c.getTime());
        }


        if(productName.equals(FASULYE)){
            setWetland(false);
            setDisinfection(false);

            this.ripeningTime = 40;
            c.add(Calendar.DATE, 40);
            setCropEnd(c.getTime());

            c.set(2019, 4, 1);
            setDisinfectionDate(c.getTime());
        }

        if(productName.equals(DOMATES)){
            setWetland(false);
            setDisinfection(false);

            this.ripeningTime = 50;
            c.add(Calendar.DATE, 50);
            setCropEnd(c.getTime());

            c.set(2019, 5, 1);
            setDisinfectionDate(c.getTime());
        }

        if(productName.equals(KAVUN)){
            setWetland(true);
            setDisinfection(false);

            this.ripeningTime = 60;
            c.add(Calendar.DATE, 60);
            setCropEnd(c.getTime());

            c.set(2019, 6, 1);
            setDisinfectionDate(c.getTime());
        }


        if(productName.equals(KARPUZ)){
            setWetland(false);
            setDisinfection(false);

            this.ripeningTime = 70;
            c.add(Calendar.DATE, 70);
            setCropEnd(c.getTime());

            c.set(2019, 7, 1);
            setDisinfectionDate(c.getTime());
        }



        if(productName.equals(MISIR)){
            setWetland(false);
            setDisinfection(false);

            this.ripeningTime = 80;
            c.add(Calendar.DATE, 80);
            setCropEnd(c.getTime());

            c.set(2019, 8, 1);
            setDisinfectionDate(c.getTime());
        }

        if(productName.equals(NOHUT)){
            setWetland(false);
            setDisinfection(false);

            this.ripeningTime = 90;
            c.add(Calendar.DATE, 90);
            setCropEnd(c.getTime());

            c.set(2019, 9, 1);
            setDisinfectionDate(c.getTime());
        }


        if(productName.equals(SARIMSAK)){
            setWetland(false);
            setDisinfection(false);

            this.ripeningTime = 100;
            c.add(Calendar.DATE, 100);
            setCropEnd(c.getTime());

            c.set(2019, 10, 1);
            setDisinfectionDate(c.getTime());
        }

        ripeningLeftTime = getRipeningLeftTime();
        disinfectionLeftTime =getDisinfectionLeftTime();
        int tmp = ripeningTime;
    }

    public int getRipeningTime() {
        return ripeningTime;
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

    public void setCropBegin(Date cropBegin) {
        this.cropBegin = cropBegin;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setCropEnd(Date cropEnd) {
        this.cropEnd = cropEnd;
    }

    public void setDisinfectionDate(Date disinfectionDate) {
        this.disinfectionDate = disinfectionDate;
    }

    public void setWetland(boolean wetland) {
        this.wetland = wetland;
    }


    public String getProductName() {
        return productName;
    }

    public Date getCropBegin() {
        return cropBegin;
    }

    public Date getCropEnd() {
        return cropEnd;
    }

    public Date getDisinfectionDate() {
        return disinfectionDate;
    }

    public boolean isWetland() {
        return wetland;
    }

    public String getNote() {
        return note;
    }

    public boolean isDisinfection() {
        return isDisinfection;
    }

    public void setDisinfection(boolean disinfection) {
        isDisinfection = disinfection;
    }

    public int getRipeningLeftTime() {      //olgunlasma - crop
        Date today = new Date();
        return  (int)( (getCropEnd().getTime() - today.getTime()) /  86400 / 1000) ;
    }

    public int getDisinfectionLeftTime() {      //ilaclamaya kalan sure

        Date today = new Date();
        return (int)( (getDisinfectionDate().getTime() - today.getTime()) /  86400 / 1000) ;
    }

    public static synchronized String createID()
    {
        return String.valueOf(idCounter++);
    }


    @Override
    public String toString() {
        return "Crop Class ok";
    }
}

