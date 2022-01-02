package com.example.pff;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditActivity extends AppCompatActivity {

    Button btnSave, btnDiscard;
    String docId, petColor, petCategory, sexType, petType1;
    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    ArrayList<Pet> petArrayList = new ArrayList<>();
    ImageView imageView;
    TextView name, age, phone;
    RadioGroup sex, type;
    RadioButton male, female, give, adapt, Sex, Type;
    Spinner colorSpinner, categorySpinner;
    Map<String, Object> map;
    ArrayList<String> color;
    ArrayList<String> category;
    ArrayAdapter<String> colors;
    ArrayAdapter<String> categories;
    Uri imageData;
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        map = new HashMap<>();

        fillColor();
        fillCategory();
        getDatas();
        getFromDatabase(docId);

        imageView = findViewById(R.id.editImageView);
        name = findViewById(R.id.editTxtName);
        age = findViewById(R.id.editTextAge);
        phone = findViewById(R.id.editTextPhoneNumb);
        sex = (RadioGroup) findViewById(R.id.editSex);
        type = (RadioGroup) findViewById(R.id.editType);


        male = (RadioButton) findViewById(R.id.editMale);
        female = (RadioButton) findViewById(R.id.editFemale);
        give = (RadioButton) findViewById(R.id.editGive);
        adapt = (RadioButton) findViewById(R.id.editAdapt);


        colorSpinner = findViewById(R.id.spinnerColor);

        colors = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, color);
        colorSpinner.setAdapter(colors);
        colorSpinner.setSelection(3);
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                petColor = parentView.getItemAtPosition(position).toString();

                //your code here

            }

            public void onNothingSelected(AdapterView<?> parentView) {

                //return;
            }
        });


        categorySpinner = findViewById(R.id.spinnerCategory);
        categories = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, category);
        categorySpinner.setAdapter(categories);
        categorySpinner.setSelection(3);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                petCategory = parentView.getItemAtPosition(position).toString();


                //your code here

            }

            public void onNothingSelected(AdapterView<?> parentView) {

                //return;
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);


            }
        });


        btnSave = findViewById(R.id.editSaveChanges);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData(docId);
                finish();

            }
        });


        btnDiscard = findViewById(R.id.editDiscardChanges);
        btnDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void setDatabaseData(Map map1) {
        Picasso.get().load(map1.get("downloadUrl").toString()).into(imageView);
        name.setText(map1.get("petName").toString());
        age.setText(map1.get("petAge").toString());
        phone.setText(map1.get("contactNumber").toString());
        String petSex = map1.get("petSex").toString();
        String petType = map1.get("type").toString();
        String petCategory = map1.get("petCategory").toString();
        String petColor = map1.get("petColor").toString();
        sexType = "Female";
        petType1 = "Give";


        if (petCategory != null) {
            int spinnerPos = categories.getPosition(petCategory);
            categorySpinner.setSelection(spinnerPos);

        }
        if (petColor != null) {
            int spinnerPos = colors.getPosition(petColor);
            colorSpinner.setSelection(spinnerPos);

        }


        if (petSex.equals("Male")) {
            male.setChecked(true);
            sexType = "Male";

        } else
            female.setChecked(true);
        if (petType.equals("Adapt")) {
            adapt.setChecked(true);
            petType1 = "Adapt";

        } else
            give.setChecked(true);


    }

    private void getDatas() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            docId = bundle.getString("docId");

        }
    }

    public void updateData(String doc) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        int radioIdSex = sex.getCheckedRadioButtonId();
        int radioIdType = type.getCheckedRadioButtonId();
        Sex = (RadioButton) sex.findViewById(radioIdSex);
        Type = (RadioButton) type.findViewById(radioIdType);


        System.out.println("image data" + imageData);
        UUID uuid = UUID.randomUUID();
        String imageName = "images/" + uuid + ".jpg";
        if (imageData != null) {
            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    //Download Url
                    StorageReference newReference = firebaseStorage.getReference(imageName);
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(@NonNull Uri uri) {





                            HashMap<String, Object> postPet = new HashMap<>();
                            postPet.put("downloadUrl", uri.toString());
                            postPet.put("usermail", user.getEmail());
                            postPet.put("username", user.getDisplayName());
//        postPet.put("userPhotoUrl", user.getPhotoUrl());
                            postPet.put("petName", name.getText().toString());
                            postPet.put("petSex", Sex.getText().toString());
                            postPet.put("petAge", age.getText().toString());
                            postPet.put("contactNumber", phone.getText().toString());
                            postPet.put("type", Type.getText().toString());
                            postPet.put("petCategory", petCategory);
                            postPet.put("petColor", petColor);
                            postPet.put("date", FieldValue.serverTimestamp());

                            db.collection("Pets").document(doc).update(postPet);
                        }
                    });


                }
            });
        }
        else {

            HashMap<String, Object> postPet = new HashMap<>();
            postPet.put("usermail", user.getEmail());
            postPet.put("username", user.getDisplayName());
//        postPet.put("userPhotoUrl", user.getPhotoUrl());
            postPet.put("petName", name.getText().toString());
            postPet.put("petSex", Sex.getText().toString());
            postPet.put("petAge", age.getText().toString());
            postPet.put("contactNumber", phone.getText().toString());
            postPet.put("type", Type.getText().toString());
            postPet.put("petCategory", petCategory);
            postPet.put("petColor", petColor);
            postPet.put("date", FieldValue.serverTimestamp());

            db.collection("Pets").document(doc).update(postPet);

        }
    }


    public void getFromDatabase(String doc) {

        DocumentReference docRef = firebaseFirestore.collection("Pets").document(doc);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        map = document.getData();
                        setDatabaseData(map);


                    } else {
                        Log.d(TAG, "No such document");

                    }

                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imageData = data.getData();

            try {
                ContentResolver contentResolver = this.getContentResolver();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageData);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

}