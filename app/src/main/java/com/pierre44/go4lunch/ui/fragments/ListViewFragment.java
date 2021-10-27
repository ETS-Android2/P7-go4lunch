package com.pierre44.go4lunch.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.pierre44.go4lunch.databinding.FragmentListviewBinding;
import com.pierre44.go4lunch.ui.activity.listView.ListViewViewModel;

public class ListViewFragment extends Fragment {

    private ListViewViewModel notificationsViewModel;
    private FragmentListviewBinding binding;

    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(ListViewViewModel.class);

        binding = FragmentListviewBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}