package com.example.pff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class messagesRecyclerView extends RecyclerView.Adapter<messagesRecyclerView.MessagesHolder> {

    private ArrayList<Message> messageArrayList;

    public messagesRecyclerView(ArrayList<Message> messageArrayList) {
        this.messageArrayList = messageArrayList;
    }

    public ArrayList<Message> getMessageArrayList() {
        return messageArrayList;
    }

    public void setMessageArrayList(ArrayList<Message> messageArrayList) {
        this.messageArrayList = messageArrayList;
    }

    @NonNull
    @Override
    public MessagesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_row_layout,parent, false);
        MessagesHolder holder = new MessagesHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesHolder holder, int position) {
        holder.name.setText(messageArrayList.get(position).getUserName());
        holder.phone.setText(messageArrayList.get(position).getPhoneNumber());
        holder.petName.setText(messageArrayList.get(position).getPetName());
    }


    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }


    public class MessagesHolder extends RecyclerView.ViewHolder{

        TextView name, phone, petName;


        public MessagesHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtMsgUserName);
            phone = itemView.findViewById(R.id.txtMessageUserPhone);
            petName = itemView.findViewById(R.id.txtUserPetName);


        }
    }
}
