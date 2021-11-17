package com.pierre44.go4lunch.ui.listWorkmates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.pierre44.go4lunch.MainViewModel;
import com.pierre44.go4lunch.databinding.ActivityMainBinding;
import com.pierre44.go4lunch.databinding.FragmentListWorkmatesBinding;
import com.pierre44.go4lunch.ui.MainActivity;

public class ListWorkmatesFragment extends Fragment {

    private WorkmatesViewModel mWorkmatesViewModel;
    private FragmentListWorkmatesBinding binding;
    private MainActivity mainActivity;
    private String currentUserId;
    private ActivityMainBinding mMainBinding;
    private MainViewModel mMainViewModel;

    public static Fragment newInstance() {
        return new ListWorkmatesFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListWorkmatesBinding.inflate(inflater, container, false);
        mWorkmatesViewModel = new ViewModelProvider(this).get(WorkmatesViewModel.class);
        if (getActivity() != null) {
            mainActivity = ((MainActivity) getActivity());
            mMainViewModel = mainActivity.getViewModel();
            if (mainActivity.getCurrentUser() != null)
                currentUserId = mainActivity.getCurrentUser().getUid();
        }

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}