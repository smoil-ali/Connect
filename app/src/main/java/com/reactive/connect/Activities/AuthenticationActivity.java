package com.reactive.connect.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.reactive.connect.Fragments.InterestFragment;
import com.reactive.connect.Fragments.LoginFragment;
import com.reactive.connect.Fragments.RegisterFragment;
import com.reactive.connect.R;
import com.reactive.connect.Utils.Constants;
import com.reactive.connect.databinding.ActivityAuthenticationBinding;

public class AuthenticationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    final String TAG = AuthenticationActivity.class.getSimpleName();
    public ActivityAuthenticationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_authentication);
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this);
        if (savedInstanceState == null){
            binding.bottomNavigation.setSelectedItemId(R.id.login);
        }



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.login:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container,new LoginFragment())
                        .commit();
                return true;
            case R.id.register:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container,new RegisterFragment())
                        .commit();
                return true;
        }
        return true;
    }

    public void openInterestFragment(Bundle bundle){
        InterestFragment fragment = new InterestFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container,fragment)
                .addToBackStack(Constants.INTEREST_FRAGMENT)
                .commit();
    }

    public void openLoginFragment(){
        binding.bottomNavigation.setSelectedItemId(R.id.login);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container,new LoginFragment())
                .commit();
    }
}