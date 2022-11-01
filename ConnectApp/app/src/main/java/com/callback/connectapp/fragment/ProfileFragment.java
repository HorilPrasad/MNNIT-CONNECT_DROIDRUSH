package com.callback.connectapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import com.callback.connectapp.R;
import com.callback.connectapp.app.AppConfig;
import com.callback.connectapp.model.User;
import com.callback.connectapp.retrofit.APIClient;

import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class ProfileFragment extends Fragment {
    private AppConfig appConfig;
  ActivityResultLauncher<String>launcher;
  ImageView profileImg;
  TextView name,email,phone,regNo,course;
  FirebaseStorage storage;
    public ProfileFragment () {
        // Required empty public constructor
    }


    @Override
    public View onCreateView (LayoutInflater inflater , ViewGroup container ,
                              Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_profile , container , false);


        name=view.findViewById(R.id.name);
        email=view.findViewById(R.id.email);
        phone=view.findViewById(R.id.phone);
        regNo=view.findViewById(R.id.regno);
        course=view.findViewById(R.id.school);

        profileImg =view.findViewById(R.id.profile_user_image);
        appConfig = new AppConfig(getContext());
        Call <User> call = APIClient.getInstance().getApiInterface()
                .getUser(appConfig.getUserID());

        Log.d("userId",appConfig.getUserID());

        call.enqueue(new Callback <User>() {
            @Override
            public void onResponse (Call <User> call , Response <User> response) {
                if(response.isSuccessful()){

                    name.setText(response.body().getName());
                    email.setText(response.body().getEmail());
                    phone.setText(response.body().getPhone());
                    regNo.setText(response.body().getRegNo());
                    course.setText(response.body().getBranch());
                    Toast.makeText(getContext(), appConfig.getUserID(), Toast.LENGTH_SHORT).show();

//                    Picasso.get()
//                            .load(response.body().getImageUrl())
//                            .placeholder(R.mipmap.ic_person)
//                            .into(profileImg);
                }else{
                    Toast.makeText(getContext(), "Problem in fetching profile"+appConfig.getUserID(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure (Call <User> call , Throwable t) {
                Toast.makeText(getContext(), "Server error!", Toast.LENGTH_SHORT).show();
            }
        });

//        storage = FirebaseStorage.getInstance();
//
//        profileImg= view.findViewById(R.id.profile_image);
//
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
//
//        profileImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick (View view) {
//                launcher.launch("image/*");
//            }
//        });

        return view;
    }
}