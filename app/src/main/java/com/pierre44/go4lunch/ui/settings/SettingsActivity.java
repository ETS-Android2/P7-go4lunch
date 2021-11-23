package com.pierre44.go4lunch.ui.settings;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;

import com.pierre44.go4lunch.MainViewModel;
import com.pierre44.go4lunch.R;
import com.pierre44.go4lunch.databinding.ActivitySettingsBinding;
import com.pierre44.go4lunch.ui.BaseActivity;

public class SettingsActivity extends BaseActivity<MainViewModel> {

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
    }


    //public static class SettingsFragment extends PreferenceFragmentCompat {
    //    @Override
    //    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    //        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    //    }
    //}
}