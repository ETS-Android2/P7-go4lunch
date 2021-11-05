package com.pierre44.go4lunch.ui.workmates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.pierre44.go4lunch.databinding.FragmentWorkmatesBinding;

public class ListWorkmatesFragment extends Fragment {

    private WorkmatesViewModel mWorkmatesViewModel;
    private FragmentWorkmatesBinding binding;

    public static Fragment newInstance() {
        return new ListWorkmatesFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mWorkmatesViewModel = new ViewModelProvider(this).get(WorkmatesViewModel.class);

        binding = FragmentWorkmatesBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}