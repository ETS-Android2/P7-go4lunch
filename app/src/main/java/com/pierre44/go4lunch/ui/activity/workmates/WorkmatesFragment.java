package com.pierre44.go4lunch.ui.activity.workmates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.pierre44.go4lunch.databinding.FragmentWorkmatesBinding;

public class WorkmatesFragment extends Fragment {

    private WorkmatesViewModel homeViewModel;
    private FragmentWorkmatesBinding binding;


    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(WorkmatesViewModel.class);

        binding = FragmentWorkmatesBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}