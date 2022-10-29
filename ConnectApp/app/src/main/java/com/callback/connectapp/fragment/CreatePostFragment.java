package com.callback.connectapp.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.callback.connectapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class CreatePostFragment extends Fragment {
    ActivityResultLauncher <String> launcher;
    private Uri imageUri = null;
    private ImageView postImage;
    private EditText etPost;
    private ImageView sendBtn,attachBtn;
    private ProgressBar pb;


    private ConstraintLayout homeLayout;

    public CreatePostFragment () {
        // Required empty public constructor
    }


    @Override
    public View onCreateView (LayoutInflater inflater , ViewGroup container ,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_post , container , false);
        postImage = view.findViewById(R.id.postImage);
        etPost = view.findViewById(R.id.etPost);
        attachBtn=view.findViewById(R.id.attach);
        sendBtn=view.findViewById(R.id.sendBtn);


        launcher = registerForActivityResult(new ActivityResultContracts.GetContent()
                , new ActivityResultCallback <Uri>() {
                    @Override
                    public void onActivityResult (Uri result) {
                        postImage.setImageURI(result);

                        //storing Img in firebase storage

//                        final StorageReference reference = storage.getReference().child("profile");
//
//                        reference.putFile(result).addOnSuccessListener(new OnSuccessListener <UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess (UploadTask.TaskSnapshot taskSnapshot) {
//
//                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener <Uri>() {
//                                    @Override
//                                    public void onSuccess (Uri uri) {
//                                        // store uri in mongo db
//
//                                        Toast.makeText(getContext(),uri.toString(),Toast.LENGTH_LONG).show();
//                                    }
//                                });
//                            }
//                        });
                    }
                });

        attachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                launcher.launch("image/*");
            }
        });


        return view;
    }


}