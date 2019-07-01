package com.example.aurora.akillitarim;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * @author Sinan Elveren, Gebze Technical University
 */
public class TarlaStatus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarla_status);
    }

    public void tickClickedBack(View view) {
        Intent myIntent = new Intent(this, MainActivity.class);
        this.startActivity(myIntent);
    }


}
