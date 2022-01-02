package com.example.pff;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class petRecyclerView extends RecyclerView.Adapter<petRecyclerView.ViewHolder> {

    private ArrayList<Pet> petArrayList;

    public petRecyclerView(ArrayList<Pet> petArrayList) {
        this.petArrayList = petArrayList;
    }

    public void setPetArrayList(ArrayList<Pet> petArrayList) {
        this.petArrayList = petArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name.setText(petArrayList.get(position).getName());
        holder.age.setText("Age: "+petArrayList.get(position).getAge());
        holder.sex.setText("Sex: "+ petArrayList.get(position).getSex());
        holder.category.setText("Category: "+ petArrayList.get(position).getCategory());
        holder.type.setText("Type: " + petArrayList.get(position).getType());
        holder.color.setText("Color: " + petArrayList.get(position).getColor());
        Picasso.get().load(petArrayList.get(position).getImageUrl()).into(holder.imageView);
        holder.btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.context, PetDetailsActivity.class);
                intent.putExtra("name", holder.name.getText().toString());
                intent.putExtra("sex",holder.sex.getText().toString());
                intent.putExtra("age",holder.age.getText().toString());
                intent.putExtra("category", holder.category.getText().toString());
                intent.putExtra("type", holder.type.getText().toString());
                intent.putExtra("color", holder.color.getText().toString());
                intent.putExtra("imgUrl", petArrayList.get(position).getImageUrl());
                intent.putExtra("phone", petArrayList.get(position).getOwnerPhone());
                holder.context.startActivity(intent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return petArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, age, sex,category, type, color, phone;
        ImageView imageView;
        Context context;
        Button btnContact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.petName);
            sex = itemView.findViewById(R.id.petSex);
            age = itemView.findViewById(R.id.petAge);
            category = itemView.findViewById(R.id.petCategory);
            imageView = itemView.findViewById(R.id.imgPet);
            type = itemView.findViewById(R.id.petType);
            color = itemView.findViewById(R.id.petColor);
            context = itemView.getContext();
            btnContact = itemView.findViewById(R.id.btnContact);


        }
    }
}
