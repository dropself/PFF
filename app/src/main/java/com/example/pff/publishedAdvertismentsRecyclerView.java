package com.example.pff;

import static android.content.ContentValues.TAG;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class publishedAdvertismentsRecyclerView extends RecyclerView.Adapter<publishedAdvertismentsRecyclerView.AdvertisementHolder> {

    private ArrayList<PublishedAdvertisements> publishedAdvertisements;
    private ArrayList<PublishedAdvertisements> publishedAdvertisementsArrayList;
    private FirebaseFirestore firebaseFirestore;
    String docId;
    String imgUrl;
    String willEditeDocId;

    public publishedAdvertismentsRecyclerView(ArrayList<PublishedAdvertisements> publishedAdvertisements) {
        this.publishedAdvertisements = publishedAdvertisements;
    }


    public ArrayList<PublishedAdvertisements> getPublishedAdvertisements() {
        return publishedAdvertisements;
    }

    public void setPublishedAdvertisements(ArrayList<PublishedAdvertisements> publishedAdvertisements) {
        this.publishedAdvertisements = publishedAdvertisements;
    }

    @NonNull
    @Override
    public AdvertisementHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.published_advertisement_row_layout, parent, false);
        AdvertisementHolder holder = new AdvertisementHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdvertisementHolder holder, int position) {
        imgUrl = publishedAdvertisements.get(position).getImgUrl();
        holder.petName.setText(publishedAdvertisements.get(position).getPetName());
        holder.petCategory.setText(publishedAdvertisements.get(position).getPetCategory());
        Picasso.get().load(publishedAdvertisements.get(position).getImgUrl()).into(holder.petImage);


        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                publishedAdvertisements.clear();
                firebaseFirestore = FirebaseFirestore.getInstance();

                firebaseFirestore.collection("Pets").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {

                        willEditeDocId = queryDocumentSnapshots.getDocuments().get(position).getId();
                        Intent intent = new Intent(holder.context, EditActivity.class);
                        intent.putExtra("docId", willEditeDocId);
                        holder.context.startActivity(intent);

//                        Bug Var

//                        getPublishedAnimals();

//                        notifyDataSetChanged();

                    }

                });



            }
        });





        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection("Pets").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            docId = queryDocumentSnapshots.getDocuments().get(position).getId();
                        }

                        if (docId != null) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("Pets").document(docId)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                            publishedAdvertisements.clear();
                                            getPublishedAnimals();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error deleting document", e);
                                        }
                                    });

                        }
                        notifyDataSetChanged();


                    }
                });



            }

        });


    }

    @Override
    public int getItemCount() {
        return publishedAdvertisements.size();
    }


    public class AdvertisementHolder extends RecyclerView.ViewHolder {
        TextView petName, petCategory;
        ImageView petImage;
        Button btnDelete, btnEdit;
        Context context;

        public AdvertisementHolder(@NonNull View itemView) {
            super(itemView);

            petName = itemView.findViewById(R.id.txtPublisedPetName);
            petCategory = itemView.findViewById(R.id.txtPublishedPetCategory);
            petImage = itemView.findViewById(R.id.publishedPetImage);
            btnDelete = itemView.findViewById(R.id.buttonDelete);
            btnEdit = itemView.findViewById(R.id.buttonEdit);
            context = itemView.getContext();
        }
    }


    private void getPublishedAnimals() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();


        firebaseFirestore.collection("Pets").whereEqualTo("usermail", userEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

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

                        publishedAdvertisements.add(new PublishedAdvertisements(petName, petCategory,petImageUrl));
                    }
                    notifyDataSetChanged();
                }

            }
        });

    }


}
