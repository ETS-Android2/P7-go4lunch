package com.pierre44.go4lunch.ui.mapView;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.pierre44.go4lunch.MainViewModel;
import com.pierre44.go4lunch.R;
import com.pierre44.go4lunch.databinding.ActivityMainBinding;
import com.pierre44.go4lunch.databinding.FragmentMapViewBinding;
import com.pierre44.go4lunch.ui.MainActivity;

public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    private static final String CAMERA_LOCATION = "cameraLocation";
    private static final float DEFAULT_ZOOM = 17.5f;
    private MapViewViewModel mMapViewViewModel;
    private FragmentMapViewBinding binding;
    private MainActivity mainActivity;
    private ActivityMainBinding mMainBinding;
    private MainViewModel mMainViewModel;
    private Location currentLocation;
    private GoogleMap mMap;
    private Location cameraLocation = null;

    public static Fragment newInstance() {
        return new MapViewFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapViewBinding.inflate(inflater, container, false);
        if (getActivity() != null) {
            mainActivity = ((MainActivity) getActivity());
            mMainBinding = mainActivity.getMainActivityBinding();
        }

        mMapViewViewModel = new ViewModelProvider(this).get(MapViewViewModel.class);

        binding.fragmentMapViewFabLocation.setOnClickListener(v -> {
            if (mainActivity.requestLocationAccess())
                fetchLastLocation();
        });

        return binding.getRoot();
    }

    //TODO : check for SuppressLint
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(mainActivity, R.raw.map_style));

        try {
            // Customise the styling of the base map using a JSON object defined in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            mainActivity, R.raw.map_style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        // Position the map's camera near nantes .
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(-34, 151)));

        if (ActivityCompat.checkSelfPermission(mainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
    }

    private void fetchLastLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mainActivity);
        initMap();

        if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnCompleteListener(getLocationTask -> {
            if (getLocationTask.isSuccessful()) {
                currentLocation = getLocationTask.getResult();
                if (currentLocation != null) {
                    configureMap(currentLocation);
                }
            } else {
                Toast.makeText(getActivity(), "Can't get location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initMap() {
        // Get a handle to the fragment and register the callback.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_view_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void configureMap(Location currentLocation) {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
        cameraLocation = new Location(currentLocation);
        mMap.setOnCameraMoveStartedListener(i -> {
            cameraLocation = new Location(CAMERA_LOCATION);
            cameraLocation.setLongitude(mMap.getCameraPosition().target.longitude);
            cameraLocation.setLatitude(mMap.getCameraPosition().target.latitude);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cameraLocation = null;

        binding = null;
    }

    //TODO : check for SuppressLint
    @SuppressLint("MissingPermission")
    public void onPermissionsGranted() {
        if (mMap != null)
            mMap.setMyLocationEnabled(true);
        fetchLastLocation();
    }

    public void onPermissionsDenied() {
        Snackbar.make(binding.getRoot(), "Location unavailable", BaseTransientBottomBar.LENGTH_INDEFINITE)
                .setTextColor(getResources().getColor(R.color.go4lunchWhite)).setDuration(5000).show();
    }
}