package com.example.aurora.akillitarim;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sinan Elveren, Gebze Technical University
 */
public class Statistic extends Fragment {
    View view;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    private ListView listView;
    private FloatingActionButton plusButton;
    private List<String> pList;
    private String[] productsList;

    private Dialog addProducts;
    private Spinner products;


    ProgressBar progressBar;

    private final String ACTUEL = "actuel";
    private final String PAST = "past";
    private final String USERS = "users";
    private final String INFIELD = "infield";



    private String cropDateString;

    //database create
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference databaseRef;
    DatabaseReference databaseInfield;

    ArrayList<String> pastPoductsArr = new ArrayList();
    ArrayList<Crop> cropsPast = new ArrayList<>();
    Map<String, Crop> productsMapPast = new HashMap<>();



    public Statistic() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.products,container,false);

        initialUI();

        return view;
    }


    private void updateProducts(){

        pList = new ArrayList<>();
     //   productsList = pList.toArray(new String[pList.size()]);

        productsList = new String[cropsPast.size()];
        int i=0;
        int index = 0;
        for (Crop value : cropsPast) {
            productsList[index] = (String) value.getProductName();
            index++;
        }

        Log.d("list VIEW PAST ", Arrays.toString(productsList));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
                android.R.id.text1, productsList);
        listView.setAdapter(adapter);


    }

    private void initialUI(){
        addProducts = new Dialog(getActivity());



        addProducts = new Dialog(getActivity());
        listView = view.findViewById(R.id.listViewProduct);
        plusButton = view.findViewById(R.id.plusButton);

        progressBar = view.findViewById(R.id.progressBar);

        //database initialize
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        getFromDataBase();
        updateProducts();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Intent intent = new Intent(getActivity(), activity_chart_pie_total.class);

                //intent.putExtra("cropListIDTotal", crops.get(position));
                intent.putExtra("cropListIDTotal", cropsPast);
                startActivity(intent);

                //  startActivity(new Intent(getActivity(),ChartPie.class));
            }
        });




    }

    private void getFromDataBase() {

        final String userEmail = firebaseAuth.getCurrentUser().getEmail().replace('.', '*');

        //  databaseRef = database.getReference("users/" + userEmail);
        final DatabaseReference dbUsers = database.getReference(USERS);

        databaseRef = dbUsers.child(userEmail);


        databaseRef.child(PAST).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // GenericTypeIndicator<Map<String, Crop>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Crop>>() {};
                    if (dataSnapshot.hasChildren()) {
                        cropsPast = new ArrayList<>();

                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            Crop crop = userSnapshot.getValue(Crop.class);
                            cropsPast.add(crop);
                        }

                       // productsMapPast = (Map<String, Crop>) dataSnapshot.getValue();
                    }

                    /*else {
                        productsMapPast = productsMap;

                        //PAST
                        databaseRef.child(PAST).setValue(productsMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Toast.makeText(getActivity(), "Bağlantı başarısız,veritabanı hatası(past)!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    */
                    updateProducts();
                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
