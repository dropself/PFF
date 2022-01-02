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

import java.util.ArrayList;

public class messages extends Fragment {

    private ArrayList<Message> messageArrayList;
    private RecyclerView recyclerView;
    private messagesRecyclerView messagesRecyclerView;


    public messages() {
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
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        setServices(view);
        fillTheArray();
        messagesRecyclerView.notifyDataSetChanged();



    }

    private void fillTheArray() {
        for (int i = 0; i<20; i++){
           messageArrayList.add(new Message("Hasan", "Cakil","12"));
           messageArrayList.add(new Message("Hasan", "Cakil","12"));
           System.out.println(messageArrayList.size());
        }
    }

    private void setServices(View view) {
        recyclerView = view.findViewById(R.id.recyclerView2);
        messageArrayList = new ArrayList<>();
        messagesRecyclerView = new messagesRecyclerView(messageArrayList);
        recyclerView.setAdapter(messagesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

}