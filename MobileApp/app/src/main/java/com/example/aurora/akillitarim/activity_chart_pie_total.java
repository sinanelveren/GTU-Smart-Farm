package com.example.aurora.akillitarim;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;



import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;


import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class activity_chart_pie_total extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    ArrayList<Crop> cropPastArrayList;


    private HorizontalBarChart skillRatingChart;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chart_pie_total);


       cropPastArrayList = (ArrayList<Crop>) getIntent().getExtras().getSerializable("cropListIDTotal");


        setSkillGraph();



        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button_total_total).setOnTouchListener(mDelayHideTouchListener);
    }

    private void setSkillGraph() {
        skillRatingChart = findViewById(R.id.skill_rating_chart) ;             //skill_rating_chart is the id of the XML layout

        skillRatingChart.setDrawBarShadow(false);
        Description description = new Description();
        description.setText("");
        skillRatingChart.setDescription(description);
        skillRatingChart.getLegend().setEnabled(false);
        skillRatingChart.setPinchZoom(false);
        skillRatingChart.setDrawValueAboveBar(false);

        //Display the axis on the left (contains the labels 1*, 2* and so on)
        XAxis xAxis = skillRatingChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setEnabled(true);
        xAxis.setDrawAxisLine(false);


        YAxis yLeft = skillRatingChart.getAxisLeft();

//Set the minimum and maximum bar lengths as per the values that they represent
        yLeft.setAxisMaximum(100f);
        yLeft.setAxisMinimum(0f);
        yLeft.setEnabled(false);

        //Set label count to 5 as we are displaying 5 star rating
        xAxis.setLabelCount(5);

//Now add the labels to be added on the vertical axis
        String[] values = {"1 *", "2 *", "3 *", "4 *", "5 *"};
        xAxis.setValueFormatter( new XAxisValueFormatter(values));

        YAxis yRight = skillRatingChart.getAxisRight();
        yRight.setDrawAxisLine(true);
        yRight.setDrawGridLines(false);
        yRight.setEnabled(false);

        //Set bar entries and add necessary formatting
        setGraphData();

        //Add animation to the graph
        skillRatingChart.animateY(2000);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }




    public class XAxisValueFormatter extends ValueFormatter {

        private String[] values;

        public XAxisValueFormatter(String[] values) {
            this.values = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            return this.values[(int) value];
        }

    }


    /**
     * Set the bar entries i.e. the percentage of users who rated the skill with
     * a certain number of stars.
     *
     * Set the colors for different bars and the bar width of the bars.
     */
    private void setGraphData() {

        //Add a list of bar entries
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int i = 0; i <cropPastArrayList.size() ; i++) {
            Log.d("asa",cropPastArrayList.get(i).getProductName() + " " + cropPastArrayList.get(i).getRipeningLeftTime()  + " " + cropPastArrayList.get(i).getRipeningTime() );

            if(cropPastArrayList.get(i).getRipeningTime() <= 0 ||  cropPastArrayList.get(i).getRipeningTime() >=365)
                entries.add((new BarEntry((float) (i+1), 0)));

            else if(cropPastArrayList.get(i).getRipeningLeftTime() <= 0)
                entries.add((new BarEntry((float) (i+1), 100)));

            else if (cropPastArrayList.get(i).getRipeningLeftTime() >= 0 && cropPastArrayList.get(i).getRipeningTime() >= 0 )

                entries.add(new BarEntry((float) (i+1),
                        (100* (  cropPastArrayList.get(i).getRipeningTime()+1 - cropPastArrayList.get(i).getRipeningLeftTime()) )/ cropPastArrayList.get(i).getRipeningTime()) );
            else
                entries.add(new BarEntry((float) (i+1), 0));
        }


/*        entries.add(new BarEntry(0.2f, 45f));
        entries.add(new BarEntry(2f, 65f));
        entries.add(new BarEntry(3f, 77f));
        entries.add(new BarEntry(4f, 93f));
*/
        //Note : These entries can be replaced by real-time data, say, from an API

        BarDataSet barDataSet = new  BarDataSet(entries, "Tum urunlerın hasata kalan zamanları");

        barDataSet.setColors(
                ContextCompat.getColor(skillRatingChart.getContext(), R.color.colorAccent),
                ContextCompat.getColor(skillRatingChart.getContext(), R.color.colorWhite),
                ContextCompat.getColor(skillRatingChart.getContext(), R.color.colorPrimaryDarkTransparent),
                ContextCompat.getColor(skillRatingChart.getContext(), R.color.colorTransparan),
                ContextCompat.getColor(skillRatingChart.getContext(), R.color.colorGrey),
                ContextCompat.getColor(skillRatingChart.getContext(), R.color.colorText));


        //Set bar shadows
        skillRatingChart.setDrawBarShadow(true);
        barDataSet.setBarShadowColor(Color.argb(40, 150, 150, 150));
        BarData data = new  BarData(barDataSet);

        //Set the bar width
        //Note : To increase the spacing between the bars set the value of barWidth to < 1f
        data.setBarWidth(0.9f);

        //Finally set the data and refresh the graph
        skillRatingChart.setData(data);
        skillRatingChart.invalidate();

    }
}



