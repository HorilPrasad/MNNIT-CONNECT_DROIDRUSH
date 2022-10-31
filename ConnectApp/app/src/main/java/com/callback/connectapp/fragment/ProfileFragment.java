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
import com.callback.connectapp.app.AppConfig;
import com.callback.connectapp.model.User;
import com.callback.connectapp.retrofit.APIClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {
    private AppConfig appConfig;
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
        appConfig = new AppConfig(getContext());
        Toast.makeText(getContext(), appConfig.getUserID(), Toast.LENGTH_SHORT).show();
        Call<User> call = APIClient.getInstance().getApiInterface().getProfile(appConfig.getUserID());

        call.enqueue(new Callback<User>() {
            @Override

            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful())
                {
                    User user = response.body();
                    Toast.makeText(getContext(), user.getName(), Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getContext(), "Not success...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

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