package com.pierre44.go4lunch.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.pierre44.go4lunch.MainViewModel;
import com.pierre44.go4lunch.R;
import com.pierre44.go4lunch.databinding.ActivitySettingsBinding;
import com.pierre44.go4lunch.models.Workmate;
import com.pierre44.go4lunch.ui.BaseActivity;

import java.util.List;
import java.util.Objects;

public class SettingsActivity extends BaseActivity<MainViewModel> {
    private Workmate currentWorkmate;
    ActivitySettingsBinding binding;

    @Override
    public Class getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    public View getLayout() {
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
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        binding.deleteAccount.setOnClickListener(v -> showDeleteAccountDialog());
        binding.settingToolbar.setNavigationOnClickListener(v->onBackPressed());
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.getWorkmate(Objects.requireNonNull(getCurrentUser()).getUid()).observe(this,this::initUI);
    }

    private void initUI(Workmate workmate) {
        currentWorkmate = workmate;
                Glide.with(binding.profilePic.getContext())
                        .load(workmate.getPhotoWorkmate())
                        .apply(RequestOptions.circleCropTransform())
                        .into(binding.profilePic);
        binding.deleteAccount.setOnClickListener(v -> showDeleteAccountDialog());
        binding.settingToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void showDeleteAccountDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.delete_account_question)
                .setPositiveButton(R.string.Delete, (paramDialogInterface, paramInt) -> {
                    viewModel.deleteWorkmate(getApplicationContext());
                    Toast.makeText(this, R.string.account_deleted, Toast.LENGTH_SHORT).show();
                    deleteAccount();
                })
                .setNegativeButton(R.string.Cancel, null)
                .show();
    }

    public void deleteAccount() {
        AuthUI.getInstance().delete(this).addOnSuccessListener(v -> backToLoginPage());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        super.onPermissionsDenied(requestCode, perms);
    }
}