package com.example.chatapp.Ui_Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.example.chatapp.Class_Structures.PhotoMessageModel;
import com.example.chatapp.R;
import com.example.chatapp.Recycler_Adapters.PhotoChatRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PhotoChat extends AppCompatActivity {
    ArrayList<PhotoMessageModel> arrayList;

    RecyclerView recyclerView;
    PhotoChatRecyclerAdapter photoChatRecyclerAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_chat);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = this.getIntent();
        final String uuid = intent.getStringExtra("chatUserUuid");
        String username = intent.getStringExtra("chatUserName");

        setTitle(username + "'s Shared Photos");

        arrayList = new ArrayList<>();

        recyclerView = findViewById(R.id.photoChatRecycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        photoChatRecyclerAdapter = new PhotoChatRecyclerAdapter(this,arrayList);
        recyclerView.setAdapter(photoChatRecyclerAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));


        FirebaseDatabase.getInstance().getReference().child("my_users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("photos").child(uuid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String message = dataSnapshot.child("message").getValue().toString();
                String url = dataSnapshot.child("url").getValue().toString();
                String sendOrReceived = dataSnapshot.child("sendOrReceived").getValue().toString();

                arrayList.add(new PhotoMessageModel(sendOrReceived,url,message));
                photoChatRecyclerAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(arrayList.size() - 1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
