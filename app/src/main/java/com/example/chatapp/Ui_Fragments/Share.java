package com.example.chatapp.Ui_Fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.chatapp.R;
import com.example.chatapp.Recycler_Adapters.ShareRecyclerAdapter;
import com.example.chatapp.Class_Structures.SendMessageUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class Share extends Fragment {

    public  ArrayList<SendMessageUser> usersList;

    public Share() {
        // Required empty public constructor
    }

    final int READ_EXTERNAL_PERMISSION_CODE = 1;
    final int GET_ACTIVITY_RESULT_CODE = 2;
    public static ImageView shareImage;
    public static EditText shareCaption;
    public static Bitmap bitmap;

    RecyclerView recyclerView;
    static ShareRecyclerAdapter shareRecyclerAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        usersList = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_share, container, false);

        shareCaption = view.findViewById(R.id.shareCaption);
        shareImage = view.findViewById(R.id.shareImage);

        recyclerView = view.findViewById(R.id.shareRecycler);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        shareRecyclerAdapter = new ShareRecyclerAdapter(getContext(),usersList);
        recyclerView.setAdapter(shareRecyclerAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        fetchUsers();

        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    loadAndSelectImage();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_PERMISSION_CODE);
                }
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_EXTERNAL_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_PERMISSION_CODE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GET_ACTIVITY_RESULT_CODE && resultCode == RESULT_OK && data != null){
                Uri choosenImageData = data.getData();

                try{
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),choosenImageData);
                    shareImage.setImageBitmap(bitmap);
                }catch (Exception e){
                    e.printStackTrace();
                }

        }
    }

    public void loadAndSelectImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,GET_ACTIVITY_RESULT_CODE);
    }


    public void fetchUsers(){
        FirebaseDatabase.getInstance().getReference().child("my_users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String username = dataSnapshot.child("username").getValue().toString();
                String uuid = dataSnapshot.getKey();

                if(!uuid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    usersList.add(new SendMessageUser(username, uuid));

                    shareRecyclerAdapter.notifyDataSetChanged();
                }
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
