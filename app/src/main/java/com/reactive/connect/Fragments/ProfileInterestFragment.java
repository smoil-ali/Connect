package com.reactive.connect.Fragments;

import android.app.ProgressDialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.reactive.connect.Activities.AuthenticationActivity;
import com.reactive.connect.Activities.HomeActivity;
import com.reactive.connect.Adapter.InterestAdapter;
import com.reactive.connect.Interfaces.InterestListener;
import com.reactive.connect.R;
import com.reactive.connect.Utils.Constants;
import com.reactive.connect.databinding.InterestFragmentBinding;
import com.reactive.connect.databinding.ProfileInterestFragmentBinding;
import com.reactive.connect.model.ProfileClass;

import java.util.ArrayList;
import java.util.List;

public class ProfileInterestFragment extends Fragment implements InterestListener {
    final String TAG = ProfileInterestFragment.class.getSimpleName();
    List<String> list = new ArrayList<>();
    List<String> interestList = new ArrayList<>();
    ProfileInterestFragmentBinding binding;
    InterestAdapter adapter;
    HomeActivity activity;
    ProfileClass model;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.USERS);
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_interest_fragment,container,false);
        activity = (HomeActivity) getActivity();
        binding.recycler.hasFixedSize();


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Register");
        progressDialog.setMessage("Wait,while your data is uploading...");

        if (getArguments() != null){
            model = (ProfileClass)getArguments().getSerializable(Constants.PARAMS);
        }

        //adapter = new InterestAdapter(getContext(),list,model.getInterest());
        binding.recycler.setAdapter(adapter);
        adapter.setListener(this);

        binding.back.setOnClickListener(v -> {
            activity.onBackPressed();
        });

        binding.done.setOnClickListener(v -> {
            if (interestList.size() > 0){
                model.setInterest(interestList);
                setUser();
            }else {
                Toast.makeText(activity, "Select at least one interest", Toast.LENGTH_SHORT).show();
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadList();
        adapter.notifyDataSetChanged();
    }

    void loadList(){
        list.add("Accounting & Tax Services");
        list.add("Arts, Culture & Entertainment");
        list.add("Auto Sales & Service");
        list.add("Banking & Finance");
        list.add("Business Services");
        list.add("Community Organizations");
        list.add("Dentists & Orthodontists");
        list.add("Education");
        list.add("Health & Wellness");
        list.add("Health Care");
        list.add("Home Improvement");
        list.add("Insurance");
        list.add("Internet & Web Services");
        list.add("Legal Services");
        list.add("Lodging & Travel");
        list.add("Marketing & Advertising");
        list.add("News & Media");
        list.add("Pet Services");
        list.add("Real Estate");
        list.add("Restaurants & Nightlife");
        list.add("Shopping & Retail");
        list.add("Web development");
        list.add("Sports & Recreation");
        list.add("Transportation Utilities");
        list.add("Wedding, Events & Meetings");
        list.add("It Services");
        list.add("Mobile development");
    }

    @Override
    public void onInterestChecked(String interest) {
        interestList.add(interest);
    }

    @Override
    public void onInterestUnChecked(String interest) {
        Log.i(TAG,interestList.size() + "list size");
        interestList.remove(interest);
        Log.i(TAG,interestList.size()+"list size");
    }

    void setUser(){
        progressDialog.show();
        databaseReference.push()
                .setValue(model)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        //activity.openLoginFragment();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.i(TAG,e.getMessage());
                    }
                });
    }}
