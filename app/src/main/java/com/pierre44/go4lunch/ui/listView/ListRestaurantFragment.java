package com.pierre44.go4lunch.ui.listView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pierre44.go4lunch.MainViewModel;
import com.pierre44.go4lunch.databinding.FragmentListviewBinding;
import com.pierre44.go4lunch.ui.MainActivity;

public class ListRestaurantFragment extends Fragment {

    private ListRestaurantViewModel mListRestaurantViewModel;
    private FragmentListviewBinding binding;
    private MainActivity context;
    private MainViewModel mMainViewModel;
    private LinearLayoutManager mLinearLayoutManager;

    public static Fragment newInstance() {
        return new ListRestaurantFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        mListRestaurantViewModel = new ViewModelProvider(this).get(ListRestaurantViewModel.class);

        binding = FragmentListviewBinding.inflate(inflater, container, false);
        if (getActivity() != null)
            context = (MainActivity) getActivity();
        mMainViewModel = context.getViewModel();
        context.binding.progressBar.setVisibility(View.VISIBLE);
        initSearchBar();
        configureRecyclerView();

        return binding.getRoot();
    }

    private void initSearchBar() {

    }

    private void configureRecyclerView() {
        mLinearLayoutManager = new LinearLayoutManager(getContext());

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}