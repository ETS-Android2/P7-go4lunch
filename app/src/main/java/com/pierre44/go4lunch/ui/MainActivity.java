package com.pierre44.go4lunch.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pierre44.go4lunch.MainViewModel;
import com.pierre44.go4lunch.R;
import com.pierre44.go4lunch.databinding.ActivityMainBinding;
import com.pierre44.go4lunch.manager.WorkmateManager;
import com.pierre44.go4lunch.ui.listView.ListRestaurantFragment;
import com.pierre44.go4lunch.ui.mapView.MapViewFragment;
import com.pierre44.go4lunch.ui.workmates.ListWorkmatesFragment;

public class MainActivity extends BaseActivity<MainViewModel> implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String ACTIVITY_MY_LUNCH = "ACTIVITY_MY_LUNCH";
    public static final String ACTIVITY_SETTINGS = "ACTIVITY_SETTINGS";
    public static final String FRAGMENT_MAP_VIEW = "FRAGMENT_MAP_VIEW";
    public static final String FRAGMENT_RESTAURANT_LIST_VIEW = "FRAGMENT_RESTAURANT_LIST_VIEW";
    public static final String FRAGMENT_WORKMATES_LIST = "FRAGMENT_WORKMATES_LIST";
    //animate navigation drawer
    private static final float END_SCALE = 0.75f;
    public ActivityMainBinding binding;
    private String currentUser;
    //DESIGN
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavView;
    private CoordinatorLayout contentView;
    // FRAGMENTS
    private Fragment fragmentMapView;
    private Fragment fragmentRestaurantList;
    private Fragment fragmentWorkmatesList;
    // MANAGER
    private WorkmateManager mWorkmateManager = WorkmateManager.getInstance();
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Configure all views
        configureToolBar();
        configureNavigation();

        // Show First Fragment
        //this.showFirstFragment();

        //initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();

    }

    private void showFirstFragment() {
        Fragment visibleFragment = getSupportFragmentManager().findFragmentById(R.id.map_view_fragment);
        if (visibleFragment == null) {
            // Show News Fragment
            this.showFragmentOrActivity(FRAGMENT_MAP_VIEW);
            // Mark as selected the menu item corresponding to NewsFragment
            this.navigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //--------------------------------------------------
    // SET UP UI
    //--------------------------------------------------

    private void configureToolBar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
    }

    private void configureNavigation() {

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavView = findViewById(R.id.bottom_nav_view);
        contentView = findViewById(R.id.content_view);

        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_your_lunch,
                R.id.nav_setting,
                R.id.nav_logout
                ).setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(bottomNavView, navController);

        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    protected Class getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    public View getLayout() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    public ActivityMainBinding getMainActivityBinding() {
        return binding;
    }

    //--------------------------------------------------
    // Navigation drawer selection
    //--------------------------------------------------
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Show fragment after user clicked on a menu item
        switch (id) {
            case R.id.nav_your_lunch:
                this.showFragmentOrActivity(ACTIVITY_MY_LUNCH);
                break;
            case R.id.nav_setting:
                this.showFragmentOrActivity(ACTIVITY_SETTINGS);
                break;
            case R.id.nav_logout:
                this.signOut();
                break;
            case R.id.menu_nav_map_view:
                this.showFragmentOrActivity(FRAGMENT_MAP_VIEW);
                break;
            case R.id.menu_nav_list_view:
                this.showFragmentOrActivity(FRAGMENT_RESTAURANT_LIST_VIEW);
                break;
            case R.id.menu_nav_workmates:
                this.showFragmentOrActivity(FRAGMENT_WORKMATES_LIST);
            default:
                break;
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private void showFragmentOrActivity(String drawerIdentifier) {
        if (ACTIVITY_MY_LUNCH.equals(drawerIdentifier)) {
            Toast.makeText(this, "Create WorkmateDetailActivity", Toast.LENGTH_LONG).show();
            // TODO create WorkmateDetailActivity
            //this.startNewActivity(WorkmateDetailActivity.class);
        } else if (ACTIVITY_SETTINGS.equals(drawerIdentifier)) {
            Toast.makeText(this, "Create SettingActivity", Toast.LENGTH_LONG).show();
            // TODO create SettingActivity
            //this.startNewActivity(SettingActivity.class);
        } else if (FRAGMENT_MAP_VIEW.equals(drawerIdentifier)) {
            this.showMapViewFragment();
        } else if (FRAGMENT_RESTAURANT_LIST_VIEW.equals(drawerIdentifier)) {
            this.showRestaurantListFragment();
        } else if (FRAGMENT_WORKMATES_LIST.equals(drawerIdentifier)) {
            this.showWorkmatesListFragment();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        //back to map view
        else if (fragmentMapView == null || !fragmentMapView.isVisible())
            bottomNavView.setSelectedItemId(R.id.menu_nav_map_view);
        else {
            super.onBackPressed();
        }
    }

    //--------------------------------------------------
    // FRAGMENTS
    //--------------------------------------------------

    private void showMapViewFragment() {
        if (this.fragmentMapView == null) this.fragmentMapView = MapViewFragment.newInstance();
        this.startTransactionFragment(this.fragmentMapView);
    }

    private void showRestaurantListFragment() {
        if (this.fragmentRestaurantList == null)
            this.fragmentRestaurantList = ListRestaurantFragment.newInstance();
        this.startTransactionFragment(this.fragmentRestaurantList);
    }

    private void showWorkmatesListFragment() {
        if (this.fragmentWorkmatesList == null)
            this.fragmentWorkmatesList = ListWorkmatesFragment.newInstance();
        this.startTransactionFragment(this.fragmentWorkmatesList);
    }

    // signOut methode with AuthUI
    public void signOut() {
        AuthUI.getInstance().signOut(this);
    }

    // Generic method that will replace and show a fragment inside the MainActivity
    private void startTransactionFragment(Fragment fragment) {
        if (!fragment.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment).commit();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}