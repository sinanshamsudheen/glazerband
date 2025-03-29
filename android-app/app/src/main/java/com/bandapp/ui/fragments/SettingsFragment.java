package com.bandapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.bandapp.R;
import com.bandapp.viewmodels.DeviceSettingsViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsFragment extends Fragment {
    private DeviceSettingsViewModel settingsViewModel;
    private SwitchMaterial switchOfflineMode;
    private SwitchMaterial switchBluetooth;
    private SwitchMaterial switchGps;
    private SwitchMaterial switchEmergencyNotifications;
    private SwitchMaterial switchLocationUpdates;
    private TextView appVersion;
    private MaterialButton btnPrivacyPolicy;
    private MaterialButton btnTermsOfService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        switchOfflineMode = view.findViewById(R.id.switch_offline_mode);
        switchBluetooth = view.findViewById(R.id.switch_bluetooth);
        switchGps = view.findViewById(R.id.switch_gps);
        switchEmergencyNotifications = view.findViewById(R.id.switch_emergency_notifications);
        switchLocationUpdates = view.findViewById(R.id.switch_location_updates);
        appVersion = view.findViewById(R.id.app_version);
        btnPrivacyPolicy = view.findViewById(R.id.btn_privacy_policy);
        btnTermsOfService = view.findViewById(R.id.btn_terms_of_service);

        // Initialize ViewModel
        settingsViewModel = new ViewModelProvider(this).get(DeviceSettingsViewModel.class);

        // Setup observers
        settingsViewModel.getCurrentSettings().observe(getViewLifecycleOwner(), settings -> {
            if (settings != null) {
                switchOfflineMode.setChecked(settings.isOfflineMode());
                switchBluetooth.setChecked(settings.isBluetoothEnabled());
                switchGps.setChecked(settings.isGpsEnabled());
                // TODO: Set other switch states
            }
        });

        // Setup click listeners
        switchOfflineMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsViewModel.updateOfflineMode(1, isChecked); // TODO: Get actual user ID
        });

        switchBluetooth.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsViewModel.updateBluetoothStatus(1, isChecked); // TODO: Get actual user ID
        });

        switchGps.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsViewModel.updateGpsStatus(1, isChecked); // TODO: Get actual user ID
        });

        btnPrivacyPolicy.setOnClickListener(v -> {
            // Open privacy policy
            // TODO: Implement privacy policy view
        });

        btnTermsOfService.setOnClickListener(v -> {
            // Open terms of service
            // TODO: Implement terms of service view
        });

        // Set app version
        appVersion.setText(getString(R.string.version));
    }
} 