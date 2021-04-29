package com.reactive.connect.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reactive.connect.Activities.AuthenticationActivity;
import com.reactive.connect.Activities.HomeActivity;
import com.reactive.connect.Activities.PostActivity;
import com.reactive.connect.R;
import com.reactive.connect.Utils.Constants;
import com.reactive.connect.Utils.Helper;
import com.reactive.connect.databinding.LoginFragmentBinding;
import com.reactive.connect.model.ProfileClass;
import com.reactive.connect.model.SignInModel;

public class LoginFragment extends Fragment {

    final String TAG = LoginFragment.class.getSimpleName();
    LoginFragmentBinding binding;
    SignInModel model;
    AuthenticationActivity activity;
    ProfileClass profileClass;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.USERS);
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment,container,false);
        activity = (AuthenticationActivity)getActivity();
        binding.setVisibility(true);
        model = new SignInModel();
        binding.setData(model);
        binding.done.setOnClickListener(v -> {
            if (isValid()){
                binding.setVisibility(false);
                check();
            }
        });



        return binding.getRoot();
    }

    boolean isValid(){
        model.setDisplayError(true);
        if (!model.getUserIdError().isEmpty()){
            return false;
        }
        if (!model.getPasswordError().isEmpty()){
            return false;
        }
        model.setDisplayError(false);
        return true;
    }

    void check(){
        firebaseAuth.signInWithEmailAndPassword(model.getUserId(),model.getPassword())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        binding.setVisibility(true);
                        openScreen();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG,e.getMessage());
                        binding.setVisibility(true);
                        Toast.makeText(activity, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    void openScreen(){
        Intent intent = new Intent(getContext(), HomeActivity.class);
        startActivity(intent);
    }
}
