package com.pierre44.go4lunch.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pierre44.go4lunch.databinding.FragmentMapviewBinding;
import com.pierre44.go4lunch.ui.activity.mapView.MapViewViewModel;

public class MapViewFragment extends Fragment {

    private MapViewViewModel dashboardViewModel;
    private FragmentMapviewBinding binding;

    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        //dashboardViewModel = new ViewModelProvider(this).get(MapViewViewModel.class);

        binding = FragmentMapviewBinding.inflate(inflater, container, false);

        //final TextView textView = binding.textMapView;
        //dashboardViewModel.getText().observe(getViewLifecycleOwner(), s -> textView.setText(s));
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}