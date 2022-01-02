package com.example.pff;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class homePage extends Fragment {

    private ArrayList<Pet> petArrayList;
    private RecyclerView recyclerView;
    private petRecyclerView petRecyclerView;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private Button buttonAll, buttonAdapt, buttonGive;
    private String categoryName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            categoryName=data.getStringExtra("result");
            petArrayList.clear();
            getDataCategory();
            System.out.println(categoryName);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton userButton = (ImageButton) view.findViewById(R.id.btnUser);
        ImageButton filterButton = (ImageButton) view.findViewById(R.id.btnFilter);


        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        setservices(view);
        getData();


        buttonAll = view.findViewById(R.id.btnAll);
        buttonAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                petArrayList.clear();
                getData();
            }
        });

        buttonAdapt = view.findViewById(R.id.btnAdapt);
        buttonAdapt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                petArrayList.clear();
                getDataAdapt();


            }
        });

        buttonGive = view.findViewById(R.id.btnGive);
        buttonGive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                petArrayList.clear();
                getDataGive();
            }
        });


//        petRecyclerView.notifyDataSetChanged();


        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int RESULT = 1;
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                startActivityForResult(intent, RESULT);
            }
        });




        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserProfile.class);
                startActivity(intent);
            }
        });


    }

    private void getDataGive() {

        firebaseFirestore.collection("Pets").whereEqualTo("type", "Give").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                if (value != null) {
                    for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                        Map<String, Object> data = documentSnapshot.getData();


                        String petImageUrl = (String) data.get("downloadUrl");
                        String petSex = (String) data.get("petSex");
                        String petName = (String) data.get("petName");
                        String petAge = (String) data.get("petAge");
                        String petCategory = (String) data.get("petCategory");
                        String petOwnerPhone = (String) data.get("contactNumber");
                        String petColor = (String) data.get("petColor");
                        String petType = (String) data.get("type");

                        petArrayList.add(new Pet(petName, petColor, petCategory, petImageUrl, petSex, petOwnerPhone, petAge, petType));

                        System.out.println(petName);
                    }
                    petRecyclerView.notifyDataSetChanged();
                }

            }
        });

    }

    private void getData() {
        firebaseFirestore.collection("Pets").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                if (value != null) {
                    for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                        Map<String, Object> data = documentSnapshot.getData();


                        String petImageUrl = (String) data.get("downloadUrl");
                        String petSex = (String) data.get("petSex");
                        String petName = (String) data.get("petName");
                        String petAge = (String) data.get("petAge");
                        String petCategory = (String) data.get("petCategory");
                        String petOwnerPhone = (String) data.get("contactNumber");
                        String petColor = (String) data.get("petColor");
                        String petType = (String) data.get("type");


                        petArrayList.add(new Pet(petName, petColor, petCategory, petImageUrl, petSex, petOwnerPhone, petAge, petType));

                        System.out.println(petName);
                    }
                    petRecyclerView.notifyDataSetChanged();
                }

            }
        });


    }


    private void getDataAdapt() {

        firebaseFirestore.collection("Pets").whereEqualTo("type", "Adapt").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                if (value != null) {
                    for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                        Map<String, Object> data = documentSnapshot.getData();


                        String petImageUrl = (String) data.get("downloadUrl");
                        String petSex = (String) data.get("petSex");
                        String petName = (String) data.get("petName");
                        String petAge = (String) data.get("petAge");
                        String petCategory = (String) data.get("petCategory");
                        String petOwnerPhone = (String) data.get("contactNumber");
                        String petColor = (String) data.get("petColor");
                        String petType = (String) data.get("type");


                        petArrayList.add(new Pet(petName, petColor, petCategory, petImageUrl, petSex, petOwnerPhone, petAge, petType));

                        System.out.println(petName);
                    }
                    petRecyclerView.notifyDataSetChanged();
                }

            }
        });


    }


    private void getDataCategory() {

        firebaseFirestore.collection("Pets").whereEqualTo("petCategory", categoryName).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                if (value != null) {
                    for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                        Map<String, Object> data = documentSnapshot.getData();


                        String petImageUrl = (String) data.get("downloadUrl");
                        String petSex = (String) data.get("petSex");
                        String petName = (String) data.get("petName");
                        String petAge = (String) data.get("petAge");
                        String petCategory = (String) data.get("petCategory");
                        String petOwnerPhone = (String) data.get("contactNumber");
                        String petColor = (String) data.get("petColor");
                        String petType = (String) data.get("type");


                        petArrayList.add(new Pet(petName, petColor, petCategory, petImageUrl, petSex, petOwnerPhone, petAge, petType));

                        System.out.println(petName);
                    }
                    petRecyclerView.notifyDataSetChanged();
                }

            }
        });


    }


    private void setservices(View view) {
        recyclerView = view.findViewById(R.id.recyleView1);
        petArrayList = new ArrayList<>();
        petRecyclerView = new petRecyclerView(petArrayList);
        recyclerView.setAdapter(petRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


    }
}