package com.example.pff;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class addNewAnimal extends Fragment {
    ImageView petImage;
    TextView name, sex, contactNumber;
    TextView age;
    ArrayList<String> color;
    ArrayList<String> category;
    Button publishButton;
    Uri imageData;
    RadioGroup radioGroupSex, radioGroupType;
    RadioButton radioButtonSex, radioButtonType;
    String petCategory, petColor;


    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionResultLauncher;

    private FirebaseStorage firebaseStorage;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_animal, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        fillColor();
        fillCategory();

//Set Spinners



        Spinner colorSpinner = view.findViewById(R.id.spnColor);

        ArrayAdapter<String> colors = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, color);
        colorSpinner.setAdapter(colors);

        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                petColor = parentView.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), petColor, Toast.LENGTH_SHORT).show();

                //your code here

            }

            public void onNothingSelected(AdapterView<?> parentView) {

                //return;
            }
        });



        Spinner categorySpinner = view.findViewById(R.id.spnCategory);
        ArrayAdapter<String> categories = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, category);
        categorySpinner.setAdapter(categories);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                petCategory = parentView.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), petCategory, Toast.LENGTH_SHORT).show();

                //your code here

            }

            public void onNothingSelected(AdapterView<?> parentView) {

                //return;
            }
        });


        radioGroupSex = (RadioGroup) view.findViewById(R.id.radioGroupSex);
        radioGroupType = (RadioGroup) view.findViewById(R.id.radioGrupType);

        name = view.findViewById(R.id.txtPetName);
        age = view.findViewById(R.id.editTxtAge);
        contactNumber = view.findViewById(R.id.editTextPhone);
        petImage = view.findViewById(R.id.imgPetPic);
        petImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

            }
        });


        publishButton = view.findViewById(R.id.btnPublish);
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadButtonClicked(view);
            }
        });


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


    }

    private void uploadButtonClicked(View view) {
//
//
        int radioIdSex = radioGroupSex.getCheckedRadioButtonId();
        int radioIdType = radioGroupType.getCheckedRadioButtonId();
        radioButtonSex = (RadioButton) radioGroupSex.findViewById(radioIdSex);
        radioButtonType = (RadioButton) radioGroupType.findViewById(radioIdType);
        //  Toast.makeText(getActivity(), radioButtonSex.getText().toString() + radioButtonType.getText().toString(), Toast.LENGTH_SHORT).show();

        System.out.println("Burası çalıştı");
        if (imageData != null) {
            System.out.println("image data Boş Değl");

            UUID uuid = UUID.randomUUID();
            String imageName = "images/" + uuid + ".jpg";
            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    //Download Url
                    StorageReference newReference = firebaseStorage.getReference(imageName);
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(@NonNull Uri uri) {


                            String downloadUri = uri.toString();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String petName = name.getText().toString();
                            String petSex = radioButtonSex.getText().toString();
                            String petAge = age.getText().toString();
                            String contactNum = contactNumber.getText().toString();
                            String type = radioButtonType.getText().toString();
                            //petCategory
                            //petColor

                            HashMap<String, Object> postPet = new HashMap<>();
                            postPet.put("downloadUrl", downloadUri);
                            postPet.put("usermail",user.getEmail());
                            postPet.put("username", user.getDisplayName());
                            //postPet.put("userPhotoUrl", user.getPhotoUrl());
                            postPet.put("petName", petName);
                            postPet.put("petSex", petSex);
                            postPet.put("petAge", petAge);
                            postPet.put("contactNumber", contactNum);
                            postPet.put("type", type);
                            postPet.put("petCategory", petCategory);
                            postPet.put("petColor", petColor);
                            postPet.put("date", FieldValue.serverTimestamp());

                            firebaseFirestore.collection("Pets").add(postPet).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(@NonNull DocumentReference documentReference) {
                                    Toast.makeText(getContext(), "Pet Has Been Added To System", Toast.LENGTH_SHORT).show();


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imageData = data.getData();

            try {
                ContentResolver contentResolver = getContext().getContentResolver();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageData);
                petImage.setImageBitmap(bitmap);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    private void setSpinners(View view) {


    }

    private void fillCategory() {
        category = new ArrayList<>();
        category.add("Dog");
        category.add("Cat");
        category.add("Horse");
        category.add("Ferret");
        category.add("Squirrel");
        category.add("Rabbit");
        category.add("Hamster");
        category.add("Bird");

    }

    private void fillColor() {
        color = new ArrayList<>();
        color.add("White");
        color.add("Chocolate");
        color.add("Caramel");
        color.add("Black");
        color.add("Dotted");
        color.add("Coffee");
        color.add("Pink");
        color.add("Orange");
        color.add("Yellow");

    }


}