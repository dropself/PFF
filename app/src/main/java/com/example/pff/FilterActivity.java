package com.example.pff;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity {
    ListView listView;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        ArrayList<String> categoriesList;

        categoriesList = new ArrayList<>();
        categoriesList.add("Dog");
        categoriesList.add("Cat");
        categoriesList.add("Horse");
        categoriesList.add("Ferret");
        categoriesList.add("Squirrel");
        categoriesList.add("Rabbit");
        categoriesList.add("Hamster");
        categoriesList.add("Bird");

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, categoriesList);

        listView = (ListView) findViewById(R.id.categoriesListView);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(FilterActivity.this, "Current Category is "+categoriesList.get(i), Toast.LENGTH_LONG).show();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", categoriesList.get(i));
                setResult(Activity.RESULT_OK,returnIntent);

                finish();
            }
        });

    }
}