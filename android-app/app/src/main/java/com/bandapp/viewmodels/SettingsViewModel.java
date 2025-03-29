package com.bandapp.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.bandapp.database.entity.DeviceSettings;
import com.bandapp.database.repository.AppRepository;

public class SettingsViewModel extends AndroidViewModel {
    private final AppRepository repository;
    private final MutableLiveData<DeviceSettings> settings;

    public SettingsViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
        settings = new MutableLiveData<>();
    }

    public LiveData<DeviceSettings> getSettings() {
        return settings;
    }

    public void loadSettings(long userId) {
        repository.getDeviceSettings(userId).observeForever(settings::setValue);
    }

    public void updateOfflineMode(boolean enabled) {
        DeviceSettings currentSettings = settings.getValue();
        if (currentSettings != null) {
            currentSettings.setOfflineMode(enabled);
            repository.updateDeviceSettings(currentSettings);
            settings.setValue(currentSettings);
        }
    }

    public void updateBluetoothStatus(boolean enabled) {
        DeviceSettings currentSettings = settings.getValue();
        if (currentSettings != null) {
            currentSettings.setBluetoothEnabled(enabled);
            repository.updateDeviceSettings(currentSettings);
            settings.setValue(currentSettings);
        }
    }

    public void updateGPSStatus(boolean enabled) {
        DeviceSettings currentSettings = settings.getValue();
        if (currentSettings != null) {
            currentSettings.setGpsEnabled(enabled);
            repository.updateDeviceSettings(currentSettings);
            settings.setValue(currentSettings);
        }
    }

    public void updateNotificationSettings(boolean enabled) {
        DeviceSettings currentSettings = settings.getValue();
        if (currentSettings != null) {
            currentSettings.setNotificationsEnabled(enabled);
            repository.updateDeviceSettings(currentSettings);
            settings.setValue(currentSettings);
        }
    }

    public void updateLocationUpdateInterval(int interval) {
        DeviceSettings currentSettings = settings.getValue();
        if (currentSettings != null) {
            currentSettings.setLocationUpdateInterval(interval);
            repository.updateDeviceSettings(currentSettings);
            settings.setValue(currentSettings);
        }
    }
} 