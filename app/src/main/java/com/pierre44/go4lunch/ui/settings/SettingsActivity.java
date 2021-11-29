package com.pierre44.go4lunch.ui.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import com.firebase.ui.auth.AuthUI;
import com.pierre44.go4lunch.MainViewModel;
import com.pierre44.go4lunch.R;
import com.pierre44.go4lunch.databinding.ActivitySettingsBinding;
import com.pierre44.go4lunch.models.Workmate;
import com.pierre44.go4lunch.ui.BaseActivity;

public class SettingsActivity extends BaseActivity<MainViewModel> {
    private Workmate currentWorkmate;
    ActivitySettingsBinding binding;

    @Override
    protected Class getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    protected View getLayout() {
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    //.replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        binding.deleteAccount.setOnClickListener(v -> showDeleteAccountDialog());
    }

    private void showDeleteAccountDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Do you want to delete your account permanently ?")
                .setPositiveButton("Delete", (paramDialogInterface, paramInt) -> {
                    //viewModel.deleteWorkmate(currentWorkmate.getIDWorkmate());
                    Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show();
                    deleteAccount();
                })
                .setNegativeButton("cancel", null)
                .show();
    }

    public void deleteAccount() {
        AuthUI.getInstance().delete(this).addOnSuccessListener(v -> backToLoginPage());
    }

    //public static class SettingsFragment extends PreferenceFragmentCompat {
    //    @Override
    //    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    //        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    //    }
    //}
}