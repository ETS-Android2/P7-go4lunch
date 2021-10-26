package com.pierre44.go4lunch.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pierre44.go4lunch.R;
import com.pierre44.go4lunch.databinding.MainActivityBinding;
import com.pierre44.go4lunch.manager.WorkmateManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends BaseActivity<MainActivityBinding> {

    MainActivityBinding binding;
    private WorkmateManager mWorkmateManager = WorkmateManager.getInstance();
    private FirebaseAuth mAuth;

    @Override
    public MainActivityBinding getViewBinding() {
        return MainActivityBinding.inflate(getLayoutInflater());
    }

    // [START auth_fui_create_launcher]
    // See: https://developer.android.com/training/basics/intents/result
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );
    // [END auth_fui_create_launcher]

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setupListeners();

        //initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();

        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
        //        R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
        //        .build();
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //NavigationUI.setupWithNavController(binding.navView, navController);
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

    private void startSignInActivity() {

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build()
        );

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }

    // [START auth_fui_result]
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            // ...
        }
            // else { Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode();
            //and handle the error.
            // ... }
    }
    // [END auth_fui_result]


    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    // ...
                });
        // [END auth_fui_signout]
    }

    public void delete() {
        // [START auth_fui_delete]
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(task -> {
                    // ...
                });
        // [END auth_fui_delete]
    }

    public void themeAndLogo() {
        List<AuthUI.IdpConfig> providers = Collections.emptyList();

        // [START auth_fui_theme_logo]
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.go4lunch_logo_text_en)    // Set logo drawable
                .setTheme(R.style.go4lunch_style)      // Set theme
                .build();
        signInLauncher.launch(signInIntent);
        // [END auth_fui_theme_logo]
    }

    public void privacyAndTerms() {
        List<AuthUI.IdpConfig> providers = Collections.emptyList();

        // [START auth_fui_pp_tos]
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTosAndPrivacyPolicyUrls(
                        "https://example.com/terms.html",
                        "https://example.com/privacy.html")
                .build();
        signInLauncher.launch(signInIntent);
        // [END auth_fui_pp_tos]
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


    // Launching MainActivity
    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}