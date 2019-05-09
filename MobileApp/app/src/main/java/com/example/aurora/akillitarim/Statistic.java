package com.example.aurora.akillitarim;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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

    private String cropDateString;

    public Statistic() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.products,container,false);

        initialUI();

        return view;
    }


    private void updateProducts(){

        productsList = pList.toArray(new String[pList.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
                android.R.id.text1, productsList);
        listView.setAdapter(adapter);
    }

    private void initialUI(){
        addProducts = new Dialog(getActivity());

        pList = new ArrayList<>();
        pList.add("Ürün seçiniz");
        pList.add("Domates");
        pList.add("Biber");
        pList.add("Patlıcan");
        pList.add("Karpuz");
        pList.add("Kavun");

        listView = view.findViewById(R.id.listViewProduct);
        plusButton =view.findViewById(R.id.plusButton);

        updateProducts();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Toast.makeText(getActivity(), productsList[position],
                        Toast.LENGTH_LONG).show();
            }
        });



    }

}
