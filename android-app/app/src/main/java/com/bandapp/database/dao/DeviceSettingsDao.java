package com.bandapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.bandapp.database.entities.DeviceSettings;
import java.util.List;

@Dao
public interface DeviceSettingsDao {
    @Insert
    long insert(DeviceSettings settings);

    @Update
    void update(DeviceSettings settings);

    @Delete
    void delete(DeviceSettings settings);

    @Query("SELECT * FROM device_settings WHERE userId = :userId")
    LiveData<DeviceSettings> getSettingsByUserId(int userId);

    @Query("UPDATE device_settings SET isOfflineMode = :isOfflineMode WHERE userId = :userId")
    void updateOfflineMode(int userId, boolean isOfflineMode);

    @Query("UPDATE device_settings SET bluetoothEnabled = :bluetoothEnabled WHERE userId = :userId")
    void updateBluetoothStatus(int userId, boolean bluetoothEnabled);

    @Query("UPDATE device_settings SET gpsEnabled = :gpsEnabled WHERE userId = :userId")
    void updateGpsStatus(int userId, boolean gpsEnabled);

    @Query("UPDATE device_settings SET lastSync = :lastSync WHERE userId = :userId")
    void updateLastSync(int userId, long lastSync);

    @Query("DELETE FROM device_settings WHERE userId = :userId")
    void deleteSettingsForUser(int userId);
} 