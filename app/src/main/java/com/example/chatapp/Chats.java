package com.example.chatapp;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 */
public class Chats extends Fragment {

    public ArrayList<SendMessageUser> usersList;

    public Chats() {
        // Required empty public constructor
    }

    RecyclerView chatsRecyclerView;
    ChatsRecylerAdapter chatsRecylerAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chats, container, false);

        view.setBackgroundColor(Color.parseColor("#f1fff0"));
        usersList = new ArrayList<>();
        chatsRecyclerView = view.findViewById(R.id.chatsRecyler);
        layoutManager = new LinearLayoutManager(getContext());
        chatsRecyclerView.setLayoutManager(layoutManager);

        chatsRecylerAdapter = new ChatsRecylerAdapter(getContext(),usersList);
        chatsRecyclerView.setAdapter(chatsRecylerAdapter);

        chatsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));


        fetchUsers();


        return view;

    }

    public void fetchUsers(){
        FirebaseDatabase.getInstance().getReference().child("my_users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String username = dataSnapshot.child("username").getValue().toString();
                String uuid = dataSnapshot.getKey();

                usersList.add(new SendMessageUser(username, uuid));

                chatsRecylerAdapter.notifyDataSetChanged();
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
