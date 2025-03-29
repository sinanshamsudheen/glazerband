package com.bandapp.ui.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bandapp.R;
import com.bandapp.ui.adapters.LocationHistoryAdapter;
import com.bandapp.utils.PermissionHandler;
import com.bandapp.viewmodels.LocationViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class LocationFragment extends Fragment implements OnMapReadyCallback {
    private static final int PERMISSION_REQUEST_CODE = 100;

    private LocationViewModel locationViewModel;
    private TextView locationStatus;
    private RecyclerView locationHistoryRecyclerView;
    private FloatingActionButton fabCenterLocation;
    private GoogleMap map;
    private LocationHistoryAdapter locationHistoryAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        locationStatus = view.findViewById(R.id.location_status);
        locationHistoryRecyclerView = view.findViewById(R.id.location_history_recycler_view);
        fabCenterLocation = view.findViewById(R.id.fab_center_location);

        // Initialize ViewModel
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);

        // Setup RecyclerView
        locationHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        locationHistoryAdapter = new LocationHistoryAdapter();
        locationHistoryRecyclerView.setAdapter(locationHistoryAdapter);

        // Setup Map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Setup observers
        locationViewModel.getLatestLocation().observe(getViewLifecycleOwner(), location -> {
            if (location != null && map != null) {
                // Update map with new location
                // TODO: Add marker or update camera position
            }
        });

        locationViewModel.getLocationHistory().observe(getViewLifecycleOwner(), locations -> {
            locationHistoryAdapter.submitList(locations);
        });

        locationViewModel.isTracking().observe(getViewLifecycleOwner(), isTracking -> {
            locationStatus.setText(isTracking ? R.string.location_tracking_active : R.string.location_tracking_inactive);
        });

        // Setup click listeners
        fabCenterLocation.setOnClickListener(v -> {
            if (map != null) {
                // Center map on current location
                // TODO: Implement center on location
            }
        });

        // Add navigation to emergency
        view.findViewById(R.id.btn_trigger_emergency).setOnClickListener(v -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_home_to_emergency);
        });

        // Check and request permissions
        checkAndRequestPermissions();
    }

    private void checkAndRequestPermissions() {
        if (!PermissionHandler.checkLocationPermissions(requireContext())) {
            PermissionHandler.requestLocationPermissions(requireActivity());
        } else {
            startLocationTracking();
        }
    }

    private void startLocationTracking() {
        // TODO: Get actual user ID
        locationViewModel.startTracking(1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                startLocationTracking();
            } else {
                Snackbar.make(requireView(), R.string.location_permission_required,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.grant_permission, v -> checkAndRequestPermissions())
                        .show();
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        locationViewModel.stopTracking();
    }
} 