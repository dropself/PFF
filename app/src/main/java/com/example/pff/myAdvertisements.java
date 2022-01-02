package com.example.pff;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;


public class myAdvertisements extends Fragment {
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<PublishedAdvertisements> publishedAdvertisementsArrayList;
    private RecyclerView recyclerView;
    private publishedAdvertismentsRecyclerView publishedAdvertismentsRecyclerView;

    public myAdvertisements() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_advertisements, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();



        setServices(view);
        getPublishedAnimals();
    }

    private void setServices(View view) {

        recyclerView = view.findViewById(R.id.recyclerView3);
        publishedAdvertisementsArrayList = new ArrayList<>();
        publishedAdvertismentsRecyclerView = new publishedAdvertismentsRecyclerView(publishedAdvertisementsArrayList);
        recyclerView.setAdapter(publishedAdvertismentsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    private void getPublishedAnimals() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();


        firebaseFirestore.collection("Pets").whereEqualTo("usermail", userEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
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

                        publishedAdvertisementsArrayList.add(new PublishedAdvertisements(petName, petCategory,petImageUrl));
                    }
                    publishedAdvertismentsRecyclerView.notifyDataSetChanged();
                }

            }
        });

    }
}