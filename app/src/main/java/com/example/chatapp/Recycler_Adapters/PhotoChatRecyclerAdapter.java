package com.example.chatapp.Recycler_Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Class_Structures.PhotoMessageModel;
import com.example.chatapp.R;

import java.util.ArrayList;

public class PhotoChatRecyclerAdapter extends RecyclerView.Adapter <PhotoChatRecyclerAdapter.PhotoChatHolder>{

    Context context;
    ArrayList<PhotoMessageModel> arrayList;

    public PhotoChatRecyclerAdapter(Context context, ArrayList<PhotoMessageModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public PhotoChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_recycler,parent,false);
        PhotoChatHolder photoChatHolder = new PhotoChatHolder(view);
        return photoChatHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoChatHolder holder, int position) {
            try{
                Glide.with(context)
                        .load(arrayList.get(position).getUrl())
                        .into(holder.photoMessage);
            }catch (Exception e){
                e.printStackTrace();
            }
            holder.photoText.setText(arrayList.get(position).getMessage());
        if(arrayList.get(position).getSendOrReceived().equals("sent")){
            holder.photoMessage.setBackgroundColor(Color.parseColor("#e5fff2"));
            holder.photoText.setBackgroundColor(Color.parseColor("#e5fff2"));
            holder.photoText.setGravity(Gravity.END);
        }else{
            holder.photoText.setBackgroundColor(Color.WHITE);
            holder.photoMessage.setBackgroundColor(Color.WHITE);
            holder.photoText.setGravity(Gravity.START);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class PhotoChatHolder extends RecyclerView.ViewHolder{

        ImageView photoMessage;
        TextView photoText;

        public PhotoChatHolder(@NonNull View itemView) {
            super(itemView);

            photoMessage = itemView.findViewById(R.id.photoMessage);
            photoText = itemView.findViewById(R.id.photoText);

        }
    }

}
