package com.example.aurora.akillitarim;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sinan Elveren, Gebze Technical University
 */
public class TarlaStatus extends AppCompatActivity  {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference databaseInfield;
    DatabaseReference databaseActuel;
    DatabaseReference databasePast;

    Switch valveControl;
    String isControllable;
    Map<String, String> infieldData = new HashMap<>();


    private final String ACTUEL = "actuel";
    private final String PAST = "past";
    private final String USERS = "users";
    private final String INFIELD = "infield";
    private final String VALVE = "valve";
    private final String CONTROLLABLE = "controllable";





    private void initializeDatabase() {
        //database initialize
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        final String userEmail = firebaseAuth.getCurrentUser().getEmail().replace('.', '*');


        final DatabaseReference dbUsers = database.getReference(USERS);

        databaseActuel = dbUsers.child(userEmail);
        databasePast = dbUsers.child(userEmail);
        databaseInfield = database.getReference(INFIELD);



        databaseInfield.addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.hasChildren()) {
                    infieldData = (Map<String, String>) snapshot.getValue();

                } else {
                    infieldData.clear();

                    databaseInfield.push().child(CONTROLLABLE);
                    databaseInfield.push().child("humidity");
                    databaseInfield.push().child("rain");
                    databaseInfield.push().child("tempeture");
                    databaseInfield.push().child("valve");
                    databaseInfield.child(CONTROLLABLE).setValue(true);
                    databaseInfield.child("control").setValue(false);
                    databaseInfield.child("humidity").setValue(false);
                    databaseInfield.child("rain").setValue(false);
                    databaseInfield.child("tempeture").setValue("24");
                    databaseInfield.child("valve").setValue("on");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TarlaStatus ", "loadPost:onCancelled ERROR", databaseError.toException());
            }
        });


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarla_status);

        valveControl = (Switch) findViewById(R.id.vanaSwitch);


        initializeDatabase();



        valveControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 // do something, the isChecked will be
                 // true if the switch is in the On position
                 if(isChecked) {
                     valveControl.setText("Vana Kontrol Kapalı");

                     databaseInfield.child(CONTROLLABLE).setValue(false ,new DatabaseReference.CompletionListener() {
                         @Override
                         public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                             if (databaseError != null) {
                                 Toast.makeText(getApplicationContext(), "Kontrol başarısız,veritabanı hatası!",Toast.LENGTH_SHORT).show();
                             } else {
                                 Toast.makeText(getApplicationContext(),"Vana kontrolü manuel",Toast.LENGTH_SHORT).show();
                             }
                         }
                     });

                    // valveControl.setText(String.valueOf(infieldData.get(CONTROLLABLE)));
                 } else{
                     valveControl.setText("Vana Kontrol Açık");

                     databaseInfield.child(CONTROLLABLE).setValue(true ,new DatabaseReference.CompletionListener() {
                         @Override
                         public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                             if (databaseError != null) {
                                 Toast.makeText(getApplicationContext(),"Kontrol başarısız,veritabanı hatası!",Toast.LENGTH_SHORT).show();
                             } else {
                                 Toast.makeText(getApplicationContext(),"Vana kontrolü otomatik",Toast.LENGTH_SHORT).show();
                             }
                         }
                     });
                 }
             }
         });
    }


    public void tickClickedBack(View view) {
        Intent myIntent = new Intent(this, HomeActivity.class);

        this.startActivity(myIntent);
        this.finish();
    }




}
