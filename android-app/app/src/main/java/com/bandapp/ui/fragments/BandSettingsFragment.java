package com.bandapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.bandapp.R;
import com.bandapp.models.BandSettings;
import com.bandapp.viewmodels.BluetoothViewModel;
import com.google.android.material.snackbar.Snackbar;

public class BandSettingsFragment extends Fragment {
    private BluetoothViewModel bluetoothViewModel;
    private Switch vibrationSwitch;
    private SeekBar vibrationIntensitySeekBar;
    private TextView vibrationIntensityText;
    private Switch soundSwitch;
    private SeekBar soundVolumeSeekBar;
    private TextView soundVolumeText;
    private SeekBar ledBrightnessSeekBar;
    private TextView ledBrightnessText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_band_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bluetoothViewModel = new ViewModelProvider(requireActivity()).get(BluetoothViewModel.class);

        // Initialize views
        vibrationSwitch = view.findViewById(R.id.vibration_switch);
        vibrationIntensitySeekBar = view.findViewById(R.id.vibration_intensity_seekbar);
        vibrationIntensityText = view.findViewById(R.id.vibration_intensity_text);
        soundSwitch = view.findViewById(R.id.sound_switch);
        soundVolumeSeekBar = view.findViewById(R.id.sound_volume_seekbar);
        soundVolumeText = view.findViewById(R.id.sound_volume_text);
        ledBrightnessSeekBar = view.findViewById(R.id.led_brightness_seekbar);
        ledBrightnessText = view.findViewById(R.id.led_brightness_text);

        // Set up listeners
        setupVibrationControls();
        setupSoundControls();
        setupLedControls();

        // Observe band settings
        bluetoothViewModel.getBandSettings().observe(getViewLifecycleOwner(), this::updateSettings);
    }

    private void setupVibrationControls() {
        vibrationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (bluetoothViewModel.isConnected().getValue() != null && bluetoothViewModel.isConnected().getValue()) {
                BandSettings settings = bluetoothViewModel.getBandSettings().getValue();
                if (settings != null) {
                    bluetoothViewModel.updateVibrationSettings(isChecked, settings.getVibrationIntensity());
                }
            } else {
                vibrationSwitch.setChecked(!isChecked);
                showNotConnectedMessage();
            }
        });

        vibrationIntensitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && bluetoothViewModel.isConnected().getValue() != null && bluetoothViewModel.isConnected().getValue()) {
                    BandSettings settings = bluetoothViewModel.getBandSettings().getValue();
                    if (settings != null) {
                        bluetoothViewModel.updateVibrationSettings(settings.isVibrationEnabled(), progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void setupSoundControls() {
        soundSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (bluetoothViewModel.isConnected().getValue() != null && bluetoothViewModel.isConnected().getValue()) {
                BandSettings settings = bluetoothViewModel.getBandSettings().getValue();
                if (settings != null) {
                    bluetoothViewModel.updateSoundSettings(isChecked, settings.getSoundVolume());
                }
            } else {
                soundSwitch.setChecked(!isChecked);
                showNotConnectedMessage();
            }
        });

        soundVolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && bluetoothViewModel.isConnected().getValue() != null && bluetoothViewModel.isConnected().getValue()) {
                    BandSettings settings = bluetoothViewModel.getBandSettings().getValue();
                    if (settings != null) {
                        bluetoothViewModel.updateSoundSettings(settings.isSoundEnabled(), progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void setupLedControls() {
        ledBrightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && bluetoothViewModel.isConnected().getValue() != null && bluetoothViewModel.isConnected().getValue()) {
                    bluetoothViewModel.updateLedBrightness(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void updateSettings(BandSettings settings) {
        if (settings != null) {
            vibrationSwitch.setChecked(settings.isVibrationEnabled());
            vibrationIntensitySeekBar.setProgress(settings.getVibrationIntensity());
            vibrationIntensityText.setText(getString(R.string.vibration_intensity_format, settings.getVibrationIntensity()));

            soundSwitch.setChecked(settings.isSoundEnabled());
            soundVolumeSeekBar.setProgress(settings.getSoundVolume());
            soundVolumeText.setText(getString(R.string.sound_volume_format, settings.getSoundVolume()));

            ledBrightnessSeekBar.setProgress(settings.getLedBrightness());
            ledBrightnessText.setText(getString(R.string.led_brightness_format, settings.getLedBrightness()));
        }
    }

    private void showNotConnectedMessage() {
        Snackbar.make(requireView(), R.string.not_connected_message, Snackbar.LENGTH_SHORT).show();
    }
} 