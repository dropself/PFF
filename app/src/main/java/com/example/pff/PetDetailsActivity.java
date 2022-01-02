package com.example.pff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.resources.TextAppearance;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class PetDetailsActivity extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    String name, category, color, age, sex, type, imgUrl, phone;
    TextView tname, tcategory, tcolor, tage, tsex, ttype;
    ImageView imageView;
    Button sendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details);

        firebaseFirestore = FirebaseFirestore.getInstance();

        sendMessage = findViewById(R.id.btnContactOwner);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Furkan Firat");
                Intent intent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+phone));
//                intent.setData();  // This ensures only SMS apps respond
                intent.putExtra("sms_body", "Hello, i am interested in your pet.");
//                intent.putExtra(Intent.EXTRA_STREAM, attachment);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });

        getSetdata();
    }

    private void getSetdata() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString("name");
            category = bundle.getString("category");
            color = bundle.getString("color");
            age = bundle.getString("age");
            sex = bundle.getString("sex");
            type = bundle.getString("type");
            imgUrl = bundle.getString("imgUrl");
            phone = bundle.getString("phone");
        }

        tname = findViewById(R.id.petNameDetails);
        tname.setText(tname.getText() + " " + name);

        tcategory = findViewById(R.id.petCategoryDetails);
        tcategory.setText(category);

        tcolor = findViewById(R.id.petColorDetails);
        tcolor.setText(color);

        tage = findViewById(R.id.petAgeDetails);
        tage.setText(age);

        tsex = findViewById(R.id.petSexDetails);
        tsex.setText(sex);

        ttype = findViewById(R.id.petTypeDetails);
        ttype.setText(type);

        imageView = findViewById(R.id.petImageDetail);

        Picasso.get().load(imgUrl).into(imageView);

    }
}