package com.pierre44.go4lunch.ui.detail;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.pierre44.go4lunch.R;
import com.pierre44.go4lunch.ui.listWorkmates.ListViewWorkmatesAdaptor;

public class DetailsActivity extends AppCompatActivity {


    RecyclerView mRecyclerViewWorkmates;
    ListViewWorkmatesAdaptor adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        this.configureToolbar();
        this.configureUI();
    }

    // Configure information into toolbar
    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbar);
        toolBarLayout.setTitle(getTitle());
    }
    // Setup UI
    private void configureUI() {

        //Detail of place

        //Recycler View
        mRecyclerViewWorkmates = findViewById(R.id.detail_activity_workmates_of_place_recycler_view);
        adapter = new ListViewWorkmatesAdaptor(this, false);
        mRecyclerViewWorkmates.setAdapter(adapter);
        mRecyclerViewWorkmates.setLayoutManager(new LinearLayoutManager(this));
    }
}