package com.bandapp.services;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.bandapp.models.BandSettings;
import java.util.UUID;

public class BluetoothService {
    // Service UUIDs
    private static final UUID BAND_SERVICE_UUID = UUID.fromString("0000180D-0000-1000-8000-00805F9B34FB");
    private static final UUID SETTINGS_SERVICE_UUID = UUID.fromString("0000180A-0000-1000-8000-00805F9B34FB");

    // Characteristic UUIDs
    private static final UUID EMERGENCY_CHARACTERISTIC_UUID = UUID.fromString("00002A37-0000-1000-8000-00805F9B34FB");
    private static final UUID VIBRATION_CHARACTERISTIC_UUID = UUID.fromString("00002A38-0000-1000-8000-00805F9B34FB");
    private static final UUID SOUND_CHARACTERISTIC_UUID = UUID.fromString("00002A39-0000-1000-8000-00805F9B34FB");
    private static final UUID LED_CHARACTERISTIC_UUID = UUID.fromString("00002A3A-0000-1000-8000-00805F9B34FB");

    private static final long SCAN_PERIOD = 10000; // 10 seconds

    private final Context context;
    private final BluetoothManager bluetoothManager;
    private final BluetoothAdapter bluetoothAdapter;
    private final BluetoothLeScanner bluetoothLeScanner;
    private final Handler scanHandler;
    private final MutableLiveData<Boolean> isScanning;
    private final MutableLiveData<Boolean> isConnected;
    private final MutableLiveData<String> connectionStatus;
    private final MutableLiveData<BandSettings> bandSettings;

    private BluetoothGatt bluetoothGatt;
    private BluetoothGattCharacteristic emergencyCharacteristic;
    private BluetoothGattCharacteristic vibrationCharacteristic;
    private BluetoothGattCharacteristic soundCharacteristic;
    private BluetoothGattCharacteristic ledCharacteristic;

    public BluetoothService(Context context) {
        this.context = context;
        this.bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        this.bluetoothAdapter = bluetoothManager.getAdapter();
        this.bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        this.scanHandler = new Handler(Looper.getMainLooper());
        this.isScanning = new MutableLiveData<>(false);
        this.isConnected = new MutableLiveData<>(false);
        this.connectionStatus = new MutableLiveData<>("Disconnected");
        this.bandSettings = new MutableLiveData<>(new BandSettings());
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
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            connectionStatus.setValue("Bluetooth is not enabled");
            return;
        }

        isScanning.setValue(true);
        connectionStatus.setValue("Scanning for band...");
        bluetoothLeScanner.startScan(scanCallback);

        scanHandler.postDelayed(this::stopScan, SCAN_PERIOD);
    }

    public void stopScan() {
        if (bluetoothLeScanner != null) {
            bluetoothLeScanner.stopScan(scanCallback);
            isScanning.setValue(false);
        }
    }

    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            // TODO: Add device name check to identify the correct band
            if (device.getName() != null && device.getName().contains("BandApp")) {
                stopScan();
                connectToDevice(device);
            }
        }
    };

    private void connectToDevice(BluetoothDevice device) {
        connectionStatus.setValue("Connecting to band...");
        bluetoothGatt = device.connectGatt(context, false, gattCallback);
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                connectionStatus.setValue("Connected");
                isConnected.setValue(true);
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                connectionStatus.setValue("Disconnected");
                isConnected.setValue(false);
                emergencyCharacteristic = null;
                vibrationCharacteristic = null;
                soundCharacteristic = null;
                ledCharacteristic = null;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // Emergency service
                BluetoothGattService emergencyService = gatt.getService(BAND_SERVICE_UUID);
                if (emergencyService != null) {
                    emergencyCharacteristic = emergencyService.getCharacteristic(EMERGENCY_CHARACTERISTIC_UUID);
                }

                // Settings service
                BluetoothGattService settingsService = gatt.getService(SETTINGS_SERVICE_UUID);
                if (settingsService != null) {
                    vibrationCharacteristic = settingsService.getCharacteristic(VIBRATION_CHARACTERISTIC_UUID);
                    soundCharacteristic = settingsService.getCharacteristic(SOUND_CHARACTERISTIC_UUID);
                    ledCharacteristic = settingsService.getCharacteristic(LED_CHARACTERISTIC_UUID);
                }

                if (emergencyCharacteristic != null && vibrationCharacteristic != null && 
                    soundCharacteristic != null && ledCharacteristic != null) {
                    connectionStatus.setValue("Ready");
                    // Read current settings
                    readBandSettings();
                }
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if (characteristic.getUuid().equals(EMERGENCY_CHARACTERISTIC_UUID)) {
                byte[] data = characteristic.getValue();
                // Handle emergency trigger from band
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if (characteristic.getUuid().equals(VIBRATION_CHARACTERISTIC_UUID)) {
                byte[] data = characteristic.getValue();
                if (data != null && data.length > 0) {
                    BandSettings settings = bandSettings.getValue();
                    settings.setVibrationEnabled(data[0] == 0x01);
                    settings.setVibrationIntensity(data[1] & 0xFF);
                    bandSettings.setValue(settings);
                }
            } else if (characteristic.getUuid().equals(SOUND_CHARACTERISTIC_UUID)) {
                byte[] data = characteristic.getValue();
                if (data != null && data.length > 0) {
                    BandSettings settings = bandSettings.getValue();
                    settings.setSoundEnabled(data[0] == 0x01);
                    settings.setSoundVolume(data[1] & 0xFF);
                    bandSettings.setValue(settings);
                }
            } else if (characteristic.getUuid().equals(LED_CHARACTERISTIC_UUID)) {
                byte[] data = characteristic.getValue();
                if (data != null && data.length > 0) {
                    BandSettings settings = bandSettings.getValue();
                    settings.setLedBrightness(data[0] & 0xFF);
                    bandSettings.setValue(settings);
                }
            }
        }
    };

    private void readBandSettings() {
        if (vibrationCharacteristic != null) {
            bluetoothGatt.readCharacteristic(vibrationCharacteristic);
        }
        if (soundCharacteristic != null) {
            bluetoothGatt.readCharacteristic(soundCharacteristic);
        }
        if (ledCharacteristic != null) {
            bluetoothGatt.readCharacteristic(ledCharacteristic);
        }
    }

    public void updateVibrationSettings(boolean enabled, int intensity) {
        if (vibrationCharacteristic != null) {
            byte[] data = new byte[]{
                enabled ? (byte)0x01 : (byte)0x00,
                (byte)(intensity & 0xFF)
            };
            vibrationCharacteristic.setValue(data);
            bluetoothGatt.writeCharacteristic(vibrationCharacteristic);
        }
    }

    public void updateSoundSettings(boolean enabled, int volume) {
        if (soundCharacteristic != null) {
            byte[] data = new byte[]{
                enabled ? (byte)0x01 : (byte)0x00,
                (byte)(volume & 0xFF)
            };
            soundCharacteristic.setValue(data);
            bluetoothGatt.writeCharacteristic(soundCharacteristic);
        }
    }

    public void updateLedBrightness(int brightness) {
        if (ledCharacteristic != null) {
            byte[] data = new byte[]{(byte)(brightness & 0xFF)};
            ledCharacteristic.setValue(data);
            bluetoothGatt.writeCharacteristic(ledCharacteristic);
        }
    }

    public void sendEmergencyTrigger() {
        if (emergencyCharacteristic != null) {
            byte[] data = new byte[]{0x01}; // Emergency trigger command
            emergencyCharacteristic.setValue(data);
            bluetoothGatt.writeCharacteristic(emergencyCharacteristic);
        }
    }

    public void disconnect() {
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            bluetoothGatt = null;
            emergencyCharacteristic = null;
            vibrationCharacteristic = null;
            soundCharacteristic = null;
            ledCharacteristic = null;
            isConnected.setValue(false);
            connectionStatus.setValue("Disconnected");
        }
    }
} 