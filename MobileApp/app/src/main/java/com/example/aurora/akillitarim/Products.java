package com.example.aurora.akillitarim;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Sinan Elveren, Gebze Technical University
 */
public class Products extends Fragment {
    DatePickerDialog.OnDateSetListener mDateSetListener;
    View view;
    private ListView listView;
    ProgressBar progressBar;
    private FloatingActionButton plusButton;
    private List<String> pList;
    private String[] actuelProductsList;
    private String[] productsList;

    private Dialog addProducts;
    private Spinner products;
    private Switch valveOnOff;

    private String cropDateString;
    private String noteString;

    //database create
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference databaseRef;
    DatabaseReference databaseInfield;

    boolean flag = true;
    private final String ACTUEL = "actuel";
    private final String PAST = "past";
    private final String USERS = "users";
    private final String INFIELD = "infield";
    private final String CONTROLLABLE = "controllable";
    private final String VALVE = "valve";

    ArrayList<String> actuelPoductsArr = new ArrayList();

    Map<String, String> infieldData = new HashMap<>();

    Map<String, Crop> productsMap = new HashMap<>();
    Map<String, Crop> productsMapPast = new HashMap<>();
    Map<String, Crop> pastproductsMap = new HashMap<>();
    ArrayList<Crop> crops = new ArrayList<>();
    ArrayList<Crop> cropsPast = new ArrayList<>();


    public Products() {
    }

    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.products,container,false);

        valveOnOff = (Switch) getActivity().findViewById(R.id.vanaOnOff);

        initialUI();

        return view;
    }
    private void updateProducts(){
        productsList = pList.toArray(new String[pList.size()]);
        //actuelProductsList = new String[productsMap.size()];
        actuelProductsList = new String[crops.size()];
        int i=0;


        int index = 0;
        for (Crop value : crops) {
            actuelProductsList[index] = (String) value.getProductName();
            index++;
        }
/*        for (int j = 0; j < crops.size() ; j++) {

            actuelProductsList[j] = (String) crops.get(j).getProductName();
        }
*/
/*
       for(Map.Entry k:productsMap.entrySet()) {
            //actuelProductsList[i]= ((String) k.getKey()).substring(3);
            //actuelPoductsArr.add(i, (String)k.getKey());
            i++;
        }
*/

        Log.d("list VIEW actuel ", Arrays.toString(actuelProductsList));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
                android.R.id.text1, actuelProductsList);
        listView.setAdapter(adapter);

    }

    private void getFromDataBase() {
        final String userEmail = firebaseAuth.getCurrentUser().getEmail().replace('.', '*');

      //  databaseRef = database.getReference("users/" + userEmail);
        final DatabaseReference dbUsers = database.getReference(USERS);

        dbUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.hasChild(userEmail)) {
                    Map<String, String> userData = new HashMap<>();
                    Crop tempCrop = new Crop(101);
                    Map<String, Crop> map = new HashMap<>();
                    map.put("initial", tempCrop);
                    userData.put(ACTUEL, "null");
                    userData.put(PAST, "null");

                    dbUsers.push().child(userEmail);
                    dbUsers.child(userEmail).setValue(userData);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseRef = dbUsers.child(userEmail);

        databaseRef.child(ACTUEL).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                   // GenericTypeIndicator<Map<String, Crop>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Crop>>() {};
                    if(dataSnapshot.hasChildren()){
                        crops = new ArrayList<>();

                        for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                            Crop crop = userSnapshot.getValue(Crop.class);
                            crops.add(crop);
                        }

                        productsMap = (Map<String, Crop>) dataSnapshot.getValue();
                        Log.d("fromDATABASE", productsMap.keySet().toString());

                    }
                    else {
                        Toast.makeText(getActivity(),"Hiç ekili ürününüz yok", Toast.LENGTH_LONG).show();
                        Log.d("DATABASE ", "is empty");
                    }

                    updateProducts();
                } //productsMap  = dataSnapshot.getValue(String.class);




               int maxEntry = 100;      //initial id
                //find max id value,for generate new product id
                for (Map.Entry<String, Crop> entry : productsMap.entrySet()) {
                    if ( Integer.parseInt(entry.getKey().replaceAll("[\\D]", "")) > maxEntry) {
                        maxEntry = Integer.parseInt(entry.getKey().replaceAll("[\\D]", ""));
                    }
                }
                if(flag){
                    flag = false;
                    Log.d("MAXCount : ", Integer.toString(maxEntry));
                    Crop tempCrop = new Crop(maxEntry+1);     //set initial id
                }
                progressBar.setVisibility(View.GONE);

          }


          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
        });




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
                    databaseInfield.push().child("control");
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

                        productsMapPast = (Map<String, Crop>) dataSnapshot.getValue();
                    }
                    else {
                        productsMapPast = productsMap;

                        //PAST
                        databaseRef.child(PAST).setValue(productsMap ,new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Toast.makeText(getActivity(),"Bağlantı başarısız,veritabanı hatası(past)!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }


                } //productsMap  = dataSnapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        }


    private void initialUI(){

        addProducts = new Dialog(getActivity());
        listView = view.findViewById(R.id.listViewProduct);
        plusButton = view.findViewById(R.id.plusButton);

        progressBar = view.findViewById(R.id.progressBar);

        //database initialize
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseInfield = database.getReference(INFIELD);




        pList = new ArrayList<>();
        pList.add("Ürün seçiniz");
        pList.add("Ay Çekirdeği");
        pList.add("Biber");
        pList.add("Buğday");
        pList.add("Fasulye");
        pList.add("Domates");
        pList.add("Kavun");
        pList.add("Karpuz");
        pList.add("Mısır");
        pList.add("Nohut");
        pList.add("Sarımsak");

        getFromDataBase();
        updateProducts();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
                refreshDatabase();

                Intent intent = new Intent(getActivity(), activity_chart_pie_total.class);


                //intent.putExtra("cropListIDTotal", crops.get(position));
                intent.putExtra("cropListIDTotal", crops);
                startActivity(intent);

              //  startActivity(new Intent(getActivity(),ChartPie.class));
            }
        });





        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addProductPopUp(getView());
            }
        });


        valveOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if(isChecked) {
                    valveOnOff.setText("Vana Kontrol Kapalı");

                    databaseInfield.child("control").setValue(false ,new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Toast.makeText(getActivity(), "Kontrol başarısız,veritabanı hatası!",Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(),"Vana Kapalı",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    // valveControl.setText(String.valueOf(infieldData.get(VALVE)));
                } else{
                    valveOnOff.setText("Vana Açık");

                    databaseInfield.child("control").setValue(true ,new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Toast.makeText(getActivity(),"Kontrol başarısız,veritabanı hatası!",Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(),"Vana Açık",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }

    private void refreshDatabase() {
        final String userEmail = firebaseAuth.getCurrentUser().getEmail().replace('.', '*');

        final DatabaseReference dbUsers = database.getReference(USERS);
        databaseRef = dbUsers.child(userEmail);
        DatabaseReference databaseRefRemove = databaseRef.child(ACTUEL);
Log.d("REFRESH, " , "1");
        //remove child, shoot to in past products
        for (int i = 0; i < crops.size(); i++) {
            Log.d("REFRESH, " , crops.get(i).getCropId());

            if ( crops.get(i).getRipeningLeftTime() <= 0 ){
               Log.d("REFRESH, " , crops.get(i).getCropId());

                databaseRefRemove.child(crops.get(i).getCropId() + crops.get(i).getProductName()).removeValue();
           }
        }

    }


    private void addProductPopUp(View view){
        TextView closeText;
        final Button cropDate;
        EditText note;
        Button addProduct;


        addProducts.setContentView(R.layout.pop_up_add_product);
        closeText = addProducts.findViewById(R.id.txtClose);
        cropDate= addProducts.findViewById(R.id.CropDate);
        products = addProducts.findViewById(R.id.productSpinner);
        note = addProducts.findViewById(R.id.addText);
        addProduct = addProducts.findViewById(R.id.addProduct);

        cropDateString = cropDate.getText().toString();


        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.addAll(Arrays.asList(productsList));

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(addProducts.getContext(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        products.setAdapter(dataAdapter);

        closeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProducts.dismiss();
            }
        });

        cropDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        addProducts.getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d("PopUp", "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                cropDate.setText(date);
            }
        };




        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("product ",cropDate.getText().toString());
                EditText note = addProducts.findViewById(R.id.addText);


                if(products.getSelectedItem().toString().equals(products.getItemAtPosition(0).toString())){
                    Toast.makeText(addProducts.getContext(), "Ürün seçiniz", Toast.LENGTH_SHORT).show();

                }else if(cropDate.getText().equals(cropDateString)) {
                    Toast.makeText(addProducts.getContext(), "Ekin zamanı ekle", Toast.LENGTH_SHORT).show();
                }else {

                    Crop newProduc = new Crop(
                            products.getSelectedItem().toString(), cropDate.getText().toString(), note.getText().toString());


                   // productsMap.put(products.getSelectedItem().toString(), newProduc);
                    productsMap.put(newProduc.getCropId() + newProduc.getProductName(), newProduc);
                    productsMapPast.put(newProduc.getCropId() + newProduc.getProductName(), newProduc);

                    progressBar.setVisibility(View.VISIBLE);
                    databaseRef.child(ACTUEL).setValue(productsMap ,new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    if (databaseError != null) {
                                        Toast.makeText(getActivity(),"Bağlantı başarısız,veritabanı hatası!",Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(),"Ürün eklendi",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    Log.d("DB PAST", productsMapPast.keySet().toString());
                    //PAST
                    databaseRef.child(PAST).setValue(productsMapPast ,new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Toast.makeText(getActivity(),"Bağlantı başarısız,veritabanı hatası(past)!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                    progressBar.setVisibility(View.INVISIBLE);

                    //new product to spinner
                    //pList.add(products.getSelectedItem().toString());
                    crops.add(newProduc);
                    cropsPast.add(newProduc);
                    updateProducts();
                    addProducts.dismiss();

                }
            }
        });



        addProducts.show();
    }
}
