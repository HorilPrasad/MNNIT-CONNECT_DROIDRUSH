package com.callback.connectapp.fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.callback.connectapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class ProfileFragment extends Fragment {

  ActivityResultLauncher<String>launcher;
  ImageView profileImg;
  FirebaseStorage storage;
    public ProfileFragment () {
        // Required empty public constructor
    }


    @Override
    public View onCreateView (LayoutInflater inflater , ViewGroup container ,
                              Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_profile , container , false);

        // Inflate the layout for this fragment
       // storage = FirebaseStorage.getInstance();

        //profileImg= view.findViewById(R.id.profile_image);

//        launcher = registerForActivityResult(new ActivityResultContracts.GetContent()
//                , new ActivityResultCallback <Uri>() {
//                    @Override
//                    public void onActivityResult (Uri result) {
//                        profileImg.setImageURI(result);
//
//                        //storing Img in firebase storage
//
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
//                    }
//                });

//        profileImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick (View view) {
//                launcher.launch("image/*");
//            }
//        });

        return view;
    }
}