package com.example.chatapp.Ui_Fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {


    private ImageView profileImageView;
    private EditText profileUsername;
    private Button uploadButton;
    public UserProfileChangeRequest userProfileChangeRequest;
    static  int GET_ACTIVITY_RESULT_CODE = 3;
    Bitmap bitmap;
    int ON_Profile_Pic = 4;
    String imageIdentifier;

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImageView = view.findViewById(R.id.profileImage);
        profileUsername = view.findViewById(R.id.profileUsername);
        uploadButton = view.findViewById(R.id.uploadButton);

        FirebaseDatabase.getInstance().getReference().child("my_users").child(FirebaseAuth.getInstance().getUid()).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    profileUsername.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("my_users").child(FirebaseAuth.getInstance().getUid()).child("profile_pic_url").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try {
                        String uri = dataSnapshot.getValue().toString();
                        Glide.with(getContext())
                                .load(uri)
                                .into(profileImageView);
                    }catch(Exception e){
                        e.printStackTrace();
                        profileImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));
                    }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "unsuccessful profile loading", Toast.LENGTH_SHORT).show();
            }
        });


        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAndSelectImage();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( !profileUsername.getText().toString().equals("") && profileImageView != null) {

                    profileImageView.setDrawingCacheEnabled(true);
                    profileImageView.buildDrawingCache();
                    bitmap = ((BitmapDrawable) profileImageView.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    FirebaseDatabase.getInstance().getReference().child("my_users").child(FirebaseAuth.getInstance().getUid()).child("username").setValue(profileUsername.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getActivity(), "Name saved", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(), "Failed to save name", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child("profile_photo").child(FirebaseAuth.getInstance().getUid()).putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getContext(), "Failed to save photo", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if(task.isSuccessful()){
                                        FirebaseDatabase.getInstance().getReference().child("my_users").child(FirebaseAuth.getInstance().getUid())
                                                .child("profile_pic_url").setValue(task.getResult().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> tsk) {
                                                if(tsk.isSuccessful()){
                                                    Toast.makeText(getActivity(), "Successful", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Toast.makeText(getActivity(), "failure", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }else{
                                        Toast.makeText(getContext(), "failed to update", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });

                }else{
                    Toast.makeText(getActivity(), "Please select both name and Photo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void loadAndSelectImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,ON_Profile_Pic);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ON_Profile_Pic && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),uri);
                if(bitmap == null) Toast.makeText(getContext(), "null bitmap", Toast.LENGTH_SHORT).show();
                else
                profileImageView.setImageBitmap(bitmap);

            }catch (Exception e){
                e.printStackTrace();
            }

        }else{
            Toast.makeText(getContext(), "Please select the photo", Toast.LENGTH_SHORT).show();
        }

    }
}
