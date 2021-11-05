package com.pierre44.go4lunch.ui.mapView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.pierre44.go4lunch.databinding.FragmentMapviewBinding;

public class MapViewFragment extends Fragment {

    private MapViewViewModel mMapViewViewModel;
    private FragmentMapviewBinding binding;

    public static Fragment newInstance() {
        return new MapViewFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        mMapViewViewModel = new ViewModelProvider(this).get(MapViewViewModel.class);

        binding = FragmentMapviewBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}