package com.reactive.connect.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.reactive.connect.Fragments.HomeFragment;
import com.reactive.connect.Fragments.InterestFragment;
import com.reactive.connect.Fragments.LoginFragment;
import com.reactive.connect.Fragments.ProfileFragment;
import com.reactive.connect.Fragments.ProfileInterestFragment;
import com.reactive.connect.Fragments.RegisterFragment;
import com.reactive.connect.R;
import com.reactive.connect.Utils.Constants;
import com.reactive.connect.databinding.ActivityHomeBinding;
import com.reactive.connect.databinding.RegisterFragmentBinding;
import com.reactive.connect.model.ProfileClass;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    final String TAG =HomeActivity.class.getSimpleName();
    ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this);
        if (savedInstanceState == null){
            binding.bottomNavigation.setSelectedItemId(R.id.profile);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container,new ProfileFragment())
                        .commit();
                return true;
            case R.id.register:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container,new RegisterFragment())
                        .commit();
                return true;
            case R.id.newsfeed:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container,new HomeFragment())
                        .commit();
                return true;
            case R.id.addpost:
                startActivity(new Intent(this, PostActivity.class));
                return true;
        }
        return true;
    }

    public void openInterestFragment(Bundle bundle){
        ProfileInterestFragment fragment = new ProfileInterestFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container,fragment)
                .addToBackStack(Constants.INTEREST_FRAGMENT)
                .commit();
    }
}