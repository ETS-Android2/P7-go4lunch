package com.pierre44.go4lunch.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.firebase.ui.auth.util.ExtraConstants;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pierre44.go4lunch.MainViewModel;
import com.pierre44.go4lunch.R;
import com.pierre44.go4lunch.databinding.ActivityAuthBinding;
import com.pierre44.go4lunch.models.Workmate;
import com.pierre44.go4lunch.ui.BaseActivity;
import com.pierre44.go4lunch.ui.MainActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AuthActivity extends BaseActivity<MainViewModel> {

    private static final int RC_SIGN_IN = 123;
    ActivityAuthBinding binding;

    // [START auth_fui_create_launcher]
    // See: https://developer.android.com/training/basics/intents/result
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );
    // [END auth_fui_create_launcher]

    @Override
    protected View getLayout() {
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected Class getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createSignInIntent();
    }

    public void createSignInIntent() {

        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build());

        // Create and launch sign-in intent

        // TODO : blur effect doesn't work
        // Blur effect on background image
        //ImageView backgroundBlur = findViewById(R.id.item_restaurant_image);
        //Glide.with(this).load(R.drawable.restaurant_background_portrait)
        //        .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
        //        .into(backgroundBlur);

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.go4lunch_logo_white_text_en)// Set logo drawable
                .setTheme(R.style.go4lunch_login) // Set theme
                .setIsSmartLockEnabled(false, true)
                .build();
        signInLauncher.launch(signInIntent);
        // [END auth_fui_create_intent]
    }

    // [START auth_fui_result]
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            checkIfUserExistInFirestore();
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Toast.makeText(this, R.string.login_success, Toast.LENGTH_LONG).show();
            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            startActivity(mainActivityIntent);
            finish();
        } else { // ERRORS
            if (response != null && response.getError() != null) {
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, getString(R.string.error_no_internet), Toast.LENGTH_LONG).show();
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    // [END auth_fui_result]

    private void checkIfUserExistInFirestore() {
        if (getCurrentUser() != null)
            viewModel.getWorkmate(getCurrentUser().getUid()).observe(this, user -> {
                if (user == null)
                    createUserInFirestore();
                else
                    startMainActivity();
            });
    }

    private void createUserInFirestore() {
        if (this.getCurrentUser() != null) {
            String iDWorkmate = getCurrentUser().getUid();
            String username = getCurrentUser().getDisplayName();
            String urlPicture = (getCurrentUser().getPhotoUrl() != null) ? getCurrentUser().getPhotoUrl().toString() : null;
            String email = getCurrentUser().getEmail();
            Workmate currentWorkmate = new Workmate(iDWorkmate, username, urlPicture, email);

            viewModel.createWorkmate(currentWorkmate);
            viewModel.getCreatedWorkmateLiveData().observe(this, user -> {
                if (user == null)
                    onFailureListener();
                else
                    startMainActivity();
            });
        }
    }


    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    public void signOut() {
        // [START auth_fui_sign_out]
        AuthUI.getInstance().signOut(this).addOnSuccessListener(aVoid -> backToLoginPage());
        ;
        // [END auth_fui_sign_out]
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

    public void emailLink() {
        // [START auth_fui_email_link]
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                .setAndroidPackageName(
                        /* yourPackageName= */ "com.pierre44.go4lunch",
                        /* installIfNotAvailable= */ true,
                        /* minimumVersion= */ null)
                .setHandleCodeInApp(true) // This must be set to true
                .setUrl("https://google.com") // This URL needs to be whitelisted
                .build();

        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.EmailBuilder()
                        .enableEmailLinkSignIn()
                        .setActionCodeSettings(actionCodeSettings)
                        .build()
        );
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
        // [END auth_fui_email_link]
    }

    public void catchEmailLink() {
        List<AuthUI.IdpConfig> providers = Collections.emptyList();

        // [START auth_fui_email_link_catch]
        if (AuthUI.canHandleIntent(getIntent())) {
            if (getIntent().getExtras() == null) {
                return;
            }
            String link = getIntent().getExtras().getString(ExtraConstants.EMAIL_LINK_SIGN_IN);
            if (link != null) {
                Intent signInIntent = AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setEmailLink(link)
                        .setAvailableProviders(providers)
                        .build();
                signInLauncher.launch(signInIntent);
            }
        }
        // [END auth_fui_email_link_catch]
    }
}