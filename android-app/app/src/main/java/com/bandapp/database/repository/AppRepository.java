package com.bandapp.database.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.bandapp.database.AppDatabase;
import com.bandapp.database.dao.*;
import com.bandapp.database.entities.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppRepository {
    private final UserDao userDao;
    private final EmergencyContactDao emergencyContactDao;
    private final LocationDao locationDao;
    private final EmergencyEventDao emergencyEventDao;
    private final DeviceSettingsDao deviceSettingsDao;
    private final ExecutorService executor;

    public AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        userDao = db.userDao();
        emergencyContactDao = db.emergencyContactDao();
        locationDao = db.locationDao();
        emergencyEventDao = db.emergencyEventDao();
        deviceSettingsDao = db.deviceSettingsDao();
        executor = Executors.newSingleThreadExecutor();
    }

    // User operations
    public void insertUser(User user) {
        executor.execute(() -> userDao.insert(user));
    }

    public void updateUser(User user) {
        executor.execute(() -> userDao.update(user));
    }

    public LiveData<User> getUserById(int userId) {
        return userDao.getUserById(userId);
    }

    public LiveData<User> getFirstUser() {
        return userDao.getFirstUser();
    }

    // Emergency Contact operations
    public void insertContact(EmergencyContact contact) {
        executor.execute(() -> emergencyContactDao.insert(contact));
    }

    public void updateContact(EmergencyContact contact) {
        executor.execute(() -> emergencyContactDao.update(contact));
    }

    public LiveData<List<EmergencyContact>> getContactsByUserId(int userId) {
        return emergencyContactDao.getContactsByUserId(userId);
    }

    // Location operations
    public void insertLocation(Location location) {
        executor.execute(() -> locationDao.insert(location));
    }

    public LiveData<Location> getLatestLocation(int userId) {
        return locationDao.getLatestLocation(userId);
    }

    public LiveData<List<Location>> getLocationsByUserId(int userId) {
        return locationDao.getLocationsByUserId(userId);
    }

    // Emergency Event operations
    public void insertEvent(EmergencyEvent event) {
        executor.execute(() -> emergencyEventDao.insert(event));
    }

    public void updateEventStatus(int eventId, String status) {
        executor.execute(() -> emergencyEventDao.updateEventStatus(eventId, status));
    }

    public LiveData<List<EmergencyEvent>> getActiveEvents() {
        return emergencyEventDao.getActiveEvents();
    }

    // Device Settings operations
    public void insertSettings(DeviceSettings settings) {
        executor.execute(() -> deviceSettingsDao.insert(settings));
    }

    public void updateOfflineMode(int userId, boolean isOfflineMode) {
        executor.execute(() -> deviceSettingsDao.updateOfflineMode(userId, isOfflineMode));
    }

    public void updateBluetoothStatus(int userId, boolean bluetoothEnabled) {
        executor.execute(() -> deviceSettingsDao.updateBluetoothStatus(userId, bluetoothEnabled));
    }

    public void updateGpsStatus(int userId, boolean gpsEnabled) {
        executor.execute(() -> deviceSettingsDao.updateGpsStatus(userId, gpsEnabled));
    }

    public LiveData<DeviceSettings> getSettingsByUserId(int userId) {
        return deviceSettingsDao.getSettingsByUserId(userId);
    }
} 