package com.example.pff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//
//        Intent intent = new Intent(this, LoginSignUp.class);
//        startActivity(intent);
//


        // Initialize Firebase Auth




        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.recyclerView, new homePage()).commit();





    }


    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuitem) {

            Fragment fragment = null;

            switch ((menuitem.getItemId())) {
                case R.id.homePage:
                    fragment = new homePage();
                    break;

                case R.id.addNewAnimal:
                    fragment = new addNewAnimal();
                    break;

                case R.id.messages:
                    fragment = new messages();
                    break;


                case R.id.myAdvertisements:
                    fragment = new myAdvertisements();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.recyclerView, fragment).commit();


            return true;
        }
    };





}