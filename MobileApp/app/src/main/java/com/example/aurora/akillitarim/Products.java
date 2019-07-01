package com.example.aurora.akillitarim;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
    private FloatingActionButton plusButton;
    private List<String> pList;
    private String[] actuelProductsList;
    private String[] productsList;

    private Dialog addProducts;
    private Spinner products;

    private String cropDateString;
    private String noteString;

    //database create
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference databaseRef;
    boolean flag = true;

    Map<String, Crop> productsDB = new HashMap<>();

    public Products() {
    }

    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.products,container,false);

        initialUI();

        return view;
    }
    private void updateProducts(){
        productsList = pList.toArray(new String[pList.size()]);
        actuelProductsList = new String[productsDB.size()];
        int i=0;

       for(Map.Entry k:productsDB.entrySet()) {
            actuelProductsList[i]= (String) k.getKey();
            i++;
        }

        Log.d("list VIEW : ", Arrays.toString(actuelProductsList));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
                android.R.id.text1, actuelProductsList);
        listView.setAdapter(adapter);
    }

    private void getFromDataBase() {
        final String tempEmail = firebaseAuth.getCurrentUser().getEmail().replace('.', '*');
        DatabaseReference userRef = databaseRef.child(tempEmail);


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                   // GenericTypeIndicator<Map<String, Crop>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Crop>>() {};
                    productsDB = (Map<String, Crop>) dataSnapshot.getValue();
                } //productsDB  = dataSnapshot.getValue(String.class);


                if (productsDB != null) {
                    Log.d("fromDATABASE", productsDB.keySet().toString());
                }
                else {
                    Log.d("DATABASE ", "is empty");
                }

               int maxEntry = 100;      //initial id
                //find max id value,for generate new product id
                for (Map.Entry<String, Crop> entry : productsDB.entrySet()) {
                    if ( Integer.parseInt(entry.getKey().replaceAll("[\\D]", "")) > maxEntry) {
                        maxEntry = Integer.parseInt(entry.getKey().replaceAll("[\\D]", ""));
                    }
                }
                if(flag){
                    flag = false;
                    Log.d("MAXCount : ", Integer.toString(maxEntry));
                    Crop tempCrop = new Crop(maxEntry+1);     //set initial id
                }
          }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        /*
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String link = ds.getValue(String.class);
                    Log.d("   TAG  ", link);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        itemsRef.addListenerForSingleValueEvent(eventListener);
 */

    }


    private void initialUI(){
        addProducts = new Dialog(getActivity());
        listView = view.findViewById(R.id.listViewProduct);
        plusButton = view.findViewById(R.id.plusButton);

        //database create
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();


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
                Toast.makeText(getActivity(), productsList[position],
                        Toast.LENGTH_LONG).show();
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addProductPopUp(getView());
            }
        });

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
                String tempEmail = firebaseAuth.getCurrentUser().getEmail().replace('.', '*');


                if(products.getSelectedItem().toString().equals(products.getItemAtPosition(0).toString())){
                    Toast.makeText(addProducts.getContext(), "Ürün seçiniz", Toast.LENGTH_SHORT).show();

                }else if(cropDate.getText().equals(cropDateString)) {
                    Toast.makeText(addProducts.getContext(), "Ekin zamanı ekle", Toast.LENGTH_SHORT).show();
                }else {

                    Crop newProduc = new Crop(
                            products.getSelectedItem().toString(), cropDate.getText().toString(), note.getText().toString());


                   // productsDB.put(products.getSelectedItem().toString(), newProduc);
                    productsDB.put(newProduc.getCropId() + newProduc.getProductName(), newProduc);
                    databaseRef.child(tempEmail).setValue(productsDB);

                    //new product to spinner
                    //pList.add(products.getSelectedItem().toString());
                    updateProducts();
                    addProducts.dismiss();
                }
            }
        });

        addProducts.show();
    }
}
