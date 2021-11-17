package com.pierre44.go4lunch.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
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
import com.pierre44.go4lunch.ui.listView.ListRestaurantsFragment;
import com.pierre44.go4lunch.ui.listWorkmates.ListWorkmatesFragment;
import com.pierre44.go4lunch.ui.mapView.MapViewFragment;

import java.util.List;
import java.util.Objects;

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
    private NavController mNavController;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private BottomNavigationView mBottomNavView;
    private CoordinatorLayout mCoordinatorLayout;
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
        configureNavigationView();

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
            this.mNavigationView.getMenu().getItem(0).setChecked(true);
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

    // Configure Toolbar
    private void configureToolBar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
    }

    // Configure Drawer Layout
    private void configureDrawerLayout(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // Configure NavigationView
    private void configureNavigation() {

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigation_view);
        mBottomNavView = findViewById(R.id.bottom_nav_view);
        mCoordinatorLayout = findViewById(R.id.content_view);

        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_your_lunch,
                R.id.nav_setting,
                R.id.nav_logout
                ).setDrawerLayout(mDrawerLayout)
                .build();
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(mNavigationView, mNavController);
        NavigationUI.setupWithNavController(mBottomNavView, mNavController);

        //animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {
        mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                mCoordinatorLayout.setScaleX(offsetScale);
                mCoordinatorLayout.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = mCoordinatorLayout.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                mCoordinatorLayout.setTranslationX(xTranslation);
            }
        });
    }

    private void configureNavigationView() {
        binding.navigationView.setNavigationItemSelectedListener(this);
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
    @SuppressLint("NonConstantResourceId")
    // NonConstantResourceId for name of menu item
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
            default:
                break;
        }
        //binding.drawerLayout.closeDrawer(GravityCompat.START);
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
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        //back to map view
        else if (fragmentMapView == null || !fragmentMapView.isVisible())
            mBottomNavView.setSelectedItemId(R.id.navigation_mapView);
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
            this.fragmentRestaurantList = ListRestaurantsFragment.newInstance();
        this.startTransactionFragment(this.fragmentRestaurantList);
    }

    private void showWorkmatesListFragment() {
        if (this.fragmentWorkmatesList == null)
            this.fragmentWorkmatesList = ListWorkmatesFragment.newInstance();
        this.startTransactionFragment(this.fragmentWorkmatesList);
    }

    // signOut methode with AuthUI
    public void signOut() {
        AuthUI.getInstance().signOut(this).addOnSuccessListener(aVoid -> backToLoginPage());;
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

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}