package com.pierre44.go4lunch.ui;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pierre44.go4lunch.MainViewModel;
import com.pierre44.go4lunch.R;
import com.pierre44.go4lunch.databinding.ActivityMainBinding;
import com.pierre44.go4lunch.manager.WorkmateManager;
import com.pierre44.go4lunch.models.PlacesPOJO;
import com.pierre44.go4lunch.models.ResultDistanceMatrix;
import com.pierre44.go4lunch.models.StoreModel;
import com.pierre44.go4lunch.placeService.PlaceApi;
import com.pierre44.go4lunch.placeService.PlaceService;
import com.pierre44.go4lunch.ui.listView.ListRestaurantsFragment;
import com.pierre44.go4lunch.ui.listWorkmates.ListViewWorkmatesAdaptor;
import com.pierre44.go4lunch.ui.listWorkmates.ListWorkmatesFragment;
import com.pierre44.go4lunch.ui.mapView.MapViewFragment;
import com.pierre44.go4lunch.ui.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity<MainViewModel> implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String ACTIVITY_MY_LUNCH = "ACTIVITY_MY_LUNCH";
    public static final String ACTIVITY_SETTINGS = "ACTIVITY_SETTINGS";
    public static final String FRAGMENT_MAP_VIEW = "FRAGMENT_MAP_VIEW";
    public static final String FRAGMENT_RESTAURANT_LIST_VIEW = "FRAGMENT_RESTAURANT_LIST_VIEW";
    public static final String FRAGMENT_WORKMATES_LIST = "FRAGMENT_WORKMATES_LIST";
    //animate navigation drawer
    private static final float END_SCALE = 0.75f;
    private final static int ALL_PERMISSIONS_RESULT = 101;
    public ActivityMainBinding binding;
    public PlacesClient placesClient;
    List<StoreModel> storeModels;
    PlaceApi apiService;
    String latLngString;
    LatLng latLng;
    RecyclerView recyclerView;
    EditText editText;
    Button button;
    List<PlacesPOJO.CustomA> results;
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
    //PLACES
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Configure all views
        configureToolBar();
        configureNavigation();
        configureNavigationView();
        initPlacesApi();
        configurePlaces();
        //configureDrawerLayout();

        // Show First Fragment
        //this.showFirstFragment();

        //initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();

    }

    //------------------//
    // Places configure //
    //------------------//

    protected void configurePlaces() {

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            else {
                fetchLocation();
            }
        } else {
            fetchLocation();
        }


        apiService = PlaceService.createService(PlaceApi.class);

        recyclerView = (RecyclerView) findViewById(R.id.fragment_list_view_recycler_view);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        editText = (EditText) findViewById(R.id.search_bar_input);
        button = (Button) findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString().trim();
                String[] split = s.split("\\s+");

                if (split.length != 2) {
                    Toast.makeText(getApplicationContext(), "Please enter text in the required format", Toast.LENGTH_SHORT).show();
                } else
                    fetchStores(split[0], split[1]);
            }
        });
    }

    private void fetchStores(String placeType, String businessName) {

        //Call<PlacesPOJO.Root> call = apiService.doPlaces(placeType, latLngString,"\""+ businessName +"\"", true, "distance", APIClient.GOOGLE_PLACE_API_KEY);

        Call<PlacesPOJO.Root> call = apiService.doPlaces(placeType, latLngString, businessName, true, "distance");
        call.enqueue(new Callback<PlacesPOJO.Root>() {
            @Override
            public void onResponse(Call<PlacesPOJO.Root> call, Response<PlacesPOJO.Root> response) {
                PlacesPOJO.Root root = response.body();

                if (response.isSuccessful()) {
                    if (root.status.equals("OK")) {
                        results = root.customA;
                        storeModels = new ArrayList<>();
                        for (int i = 0; i < results.size(); i++) {
                            if (i == 10)
                                break;
                            PlacesPOJO.CustomA info = results.get(i);

                            fetchDistance(info);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "No matches found near you", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() != 200) {
                    Toast.makeText(getApplicationContext(), "Error " + response.code() + " found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlacesPOJO.Root> call, Throwable t) {
                // Log error here since request failed
                call.cancel();
            }
        });


    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }
                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                } else {
                    fetchLocation();
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void fetchLocation() {

        SmartLocation.with(this).location()
                .oneFix()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        latLngString = location.getLatitude() + "," + location.getLongitude();
                        latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    }
                });
    }

    private void fetchDistance(final PlacesPOJO.CustomA info) {

        Call<ResultDistanceMatrix> call = apiService.getDistance(latLngString, info.geometry.locationA.lat + "," + info.geometry.locationA.lng);
        call.enqueue(new Callback<ResultDistanceMatrix>() {
            @Override
            public void onResponse(Call<ResultDistanceMatrix> call, Response<ResultDistanceMatrix> response) {
                ResultDistanceMatrix resultDistance = response.body();
                if ("OK".equalsIgnoreCase(resultDistance.status)) {
                    ResultDistanceMatrix.InfoDistanceMatrix infoDistanceMatrix = resultDistance.rows.get(0);
                    ResultDistanceMatrix.InfoDistanceMatrix.DistanceElement distanceElement = (ResultDistanceMatrix.InfoDistanceMatrix.DistanceElement) infoDistanceMatrix.elements.get(0);
                    if ("OK".equalsIgnoreCase(distanceElement.status)) {
                        ResultDistanceMatrix.InfoDistanceMatrix.ValueItem itemDuration = distanceElement.duration;
                        ResultDistanceMatrix.InfoDistanceMatrix.ValueItem itemDistance = distanceElement.distance;
                        String totalDistance = String.valueOf(itemDistance.text);
                        String totalDuration = String.valueOf(itemDuration.text);
                        storeModels.add(new StoreModel(info.name, info.vicinity, totalDistance, totalDuration));

                        if (storeModels.size() == 10 || storeModels.size() == results.size()) {
                            ListViewWorkmatesAdaptor adapterStores = new ListViewWorkmatesAdaptor(MainActivity.this);
                            recyclerView.setAdapter(adapterStores);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultDistanceMatrix> call, Throwable t) {
                call.cancel();
            }
        });

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

    private void initPlacesApi() {
        // TODO: API KEY TO BE SECURE
        Places.initialize(getApplicationContext(), String.valueOf(R.string.google_maps_key));
        placesClient = Places.createClient(this);
    }

    //-----------//
    // SET UP UI //
    //-----------//

    // Configure Toolbar
    private void configureToolBar() {
        Toolbar toolbar = findViewById(R.id.my_top_AppBar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
    }

    // Configure Drawer Layout
    private void configureDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.myTopAppBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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

    //-----------------------------//
    // Navigation drawer selection //
    //-----------------------------//

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
            this.startNewActivity(SettingsActivity.class);
        } else if (FRAGMENT_MAP_VIEW.equals(drawerIdentifier)) {
            this.showMapViewFragment();
        } else if (FRAGMENT_RESTAURANT_LIST_VIEW.equals(drawerIdentifier)) {
            this.showRestaurantListFragment();
        } else if (FRAGMENT_WORKMATES_LIST.equals(drawerIdentifier)) {
            this.showWorkmatesListFragment();
        }
    }

    private void startNewActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        EditText editText = null;

        if (item.getItemId() == R.id.search_bar_menu) {
            binding.searchBarMap.searchBarInput.setVisibility(View.VISIBLE);
            editText = binding.searchBarMap.searchBarInput;
        }
        showKeyboard(editText);
        return true;
    }

    private void showKeyboard(EditText editText) {
        if (editText != null)
            editText.requestFocus();

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null)
            inputManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
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

    //-----------//
    // FRAGMENTS //
    //-----------//

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
        AuthUI.getInstance().signOut(this).addOnSuccessListener(aVoid -> backToLoginPage());
        ;
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