package ru.ermilov.servicecenter;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class FragmentFormOrder extends Fragment {


    List<String> categories = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_form_order, container, false);

        Spinner categoryList = view.findViewById(R.id.spinnerСategory);
        //получение категорий из БД
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("Category").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            }
            else {
                for (DataSnapshot ds : task.getResult().getChildren()) {
                    categories.add(ds.getValue(Categories.class).name);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, categories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categoryList.setAdapter(adapter);
            }
        });

        EditText etDiscriptionСondition = view.findViewById(R.id.etDiscriptionСondition);
        EditText etDiscriptionProblem = view.findViewById(R.id.etDiscriptionProblem);
        EditText etDateStart = view.findViewById(R.id.etDateStart);
        EditText etDateEnd = view.findViewById(R.id.etDateEnd);

        CardView btnCreateOrder = view.findViewById(R.id.btnCreateOrder);
        btnCreateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Category = categoryList.getSelectedItem().toString();
                String DiscriptionСondition = etDiscriptionСondition.getText().toString();
                String DiscriptionProblem = etDiscriptionProblem.getText().toString();
                String DateStart = etDateStart.getText().toString();
                String DateEnd = etDateEnd.getText().toString();

                Orders order = new Orders(Category, DiscriptionСondition, DiscriptionProblem, DateStart, DateEnd);

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                db.child("Orders").push().setValue(order);
            }
        });

        CardView btnNazad = view.findViewById(R.id.button_nazad);
        btnNazad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_add_order fragment_add_order = new Fragment_add_order();
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.add_client, fragment_add_order);
                ft.commit();
            }
        });

        return view;
    }
}