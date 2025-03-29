package com.bandapp.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.bandapp.models.BandSettings;
import com.bandapp.services.BluetoothService;

public class BluetoothViewModel extends AndroidViewModel {
    private final BluetoothService bluetoothService;
    private final LiveData<Boolean> isScanning;
    private final LiveData<Boolean> isConnected;
    private final LiveData<String> connectionStatus;
    private final LiveData<BandSettings> bandSettings;

    public BluetoothViewModel(Application application) {
        super(application);
        bluetoothService = new BluetoothService(application);
        isScanning = bluetoothService.isScanning();
        isConnected = bluetoothService.isConnected();
        connectionStatus = bluetoothService.getConnectionStatus();
        bandSettings = bluetoothService.getBandSettings();
    }

    public LiveData<Boolean> isScanning() {
        return isScanning;
    }

    public LiveData<Boolean> isConnected() {
        return isConnected;
    }

    public LiveData<String> getConnectionStatus() {
        return connectionStatus;
    }

    public LiveData<BandSettings> getBandSettings() {
        return bandSettings;
    }

    public void startScan() {
        bluetoothService.startScan();
    }

    public void stopScan() {
        bluetoothService.stopScan();
    }

    public void sendEmergencyTrigger() {
        bluetoothService.sendEmergencyTrigger();
    }

    public void updateVibrationSettings(boolean enabled, int intensity) {
        bluetoothService.updateVibrationSettings(enabled, intensity);
    }

    public void updateSoundSettings(boolean enabled, int volume) {
        bluetoothService.updateSoundSettings(enabled, volume);
    }

    public void updateLedBrightness(int brightness) {
        bluetoothService.updateLedBrightness(brightness);
    }

    public void disconnect() {
        bluetoothService.disconnect();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        bluetoothService.disconnect();
    }
} 