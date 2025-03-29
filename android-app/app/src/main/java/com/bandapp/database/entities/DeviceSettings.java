package com.bandapp.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "device_settings",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "userId",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE))
public class DeviceSettings {
    @PrimaryKey(autoGenerate = true)
    private int settingId;
    
    private int userId;
    private boolean isOfflineMode;
    private boolean bluetoothEnabled;
    private boolean gpsEnabled;
    private long lastSync;

    // Constructor
    public DeviceSettings(int userId) {
        this.userId = userId;
        this.isOfflineMode = false;
        this.bluetoothEnabled = true;
        this.gpsEnabled = true;
        this.lastSync = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getSettingId() {
        return settingId;
    }

    public void setSettingId(int settingId) {
        this.settingId = settingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isOfflineMode() {
        return isOfflineMode;
    }

    public void setOfflineMode(boolean offlineMode) {
        isOfflineMode = offlineMode;
    }

    public boolean isBluetoothEnabled() {
        return bluetoothEnabled;
    }

    public void setBluetoothEnabled(boolean bluetoothEnabled) {
        this.bluetoothEnabled = bluetoothEnabled;
    }

    public boolean isGpsEnabled() {
        return gpsEnabled;
    }

    public void setGpsEnabled(boolean gpsEnabled) {
        this.gpsEnabled = gpsEnabled;
    }

    public long getLastSync() {
        return lastSync;
    }

    public void setLastSync(long lastSync) {
        this.lastSync = lastSync;
    }
} 