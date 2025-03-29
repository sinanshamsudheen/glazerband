package com.bandapp.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.bandapp.database.entity.DeviceSettings;
import com.bandapp.database.repository.AppRepository;

public class DeviceSettingsViewModel extends AndroidViewModel {
    private final AppRepository repository;
    private final LiveData<DeviceSettings> currentSettings;

    public DeviceSettingsViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
        currentSettings = repository.getDeviceSettings(1); // TODO: Get actual user ID
    }

    public LiveData<DeviceSettings> getCurrentSettings() {
        return currentSettings;
    }

    public void insertSettings(DeviceSettings settings) {
        repository.insertSettings(settings);
    }

    public void updateOfflineMode(long userId, boolean enabled) {
        repository.updateOfflineMode(userId, enabled);
    }

    public void updateBluetoothStatus(long userId, boolean enabled) {
        repository.updateBluetoothStatus(userId, enabled);
    }

    public void updateGpsStatus(long userId, boolean enabled) {
        repository.updateGpsStatus(userId, enabled);
    }

    public void updateEmergencyNotifications(long userId, boolean enabled) {
        // TODO: Implement emergency notifications setting
    }

    public void updateLocationUpdates(long userId, boolean enabled) {
        // TODO: Implement location updates setting
    }

    public void updateLastSyncTime(long userId, long timestamp) {
        repository.updateLastSyncTime(userId, timestamp);
    }

    public LiveData<DeviceSettings> getSettingsByUserId(int userId) {
        return repository.getSettingsByUserId(userId);
    }
} 