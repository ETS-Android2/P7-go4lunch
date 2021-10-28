package com.pierre44.go4lunch.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pierre44.go4lunch.R;
import com.pierre44.go4lunch.databinding.ActivityMainBinding;
import com.pierre44.go4lunch.manager.WorkmateManager;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private WorkmateManager mWorkmateManager = WorkmateManager.getInstance();
    private FirebaseAuth mAuth;


    @Override
    public ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupListeners();

        //initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        //Passing each menu ID as a set of Ids because each
        //menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    private void setupListeners() {
        // Login/Email button
        //binding.loginWithEmailButton.setOnClickListener(view -> {
        //    if (mWorkmateManager.isCurrentUserLogged()) {
        //        startMainActivity();
        //    } else {
        //        startSignInActivity();
        //    }
        //});
        // Login/Google Button
        //binding.loginWithGoogleButton.setOnClickListener(view -> {
        //    if (mWorkmateManager.isCurrentUserLogged()) {
        //        startMainActivity();
        //    } else {
        //        startSignInActivity();
        //    }
        //});
        //// Login/Facebook Button
        //binding.loginWithFacebookButton.setOnClickListener(view -> {
        //    if (mWorkmateManager.isCurrentUserLogged()) {
        //        startMainActivity();
        //    } else {
        //        startSignInActivity();
        //    }
        //});
        //// Login/Twitter Button
        //binding.loginWithTwitterButton.setOnClickListener(view -> {
        //    if (mWorkmateManager.isCurrentUserLogged()) {
        //        startMainActivity();
        //    } else {
        //        startSignInActivity();
        //    }
        //});
    }
}