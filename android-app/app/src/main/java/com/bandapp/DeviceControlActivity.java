package com.bandapp;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

public class DeviceControlActivity extends AppCompatActivity {
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private String deviceName;
    private String deviceAddress;
    private BluetoothGatt bluetoothGatt;
    private int connectionState = BluetoothProfile.STATE_DISCONNECTED;

    private TextView connectionStatus;
    private TextView deviceInfoText;
    private Button emergencyButton;
    private Button locationButton;
    private Button disconnectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control);

        final Intent intent = getIntent();
        deviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        deviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        // Set up UI
        connectionStatus = findViewById(R.id.connectionStatus);
        deviceInfoText = findViewById(R.id.deviceInfo);
        emergencyButton = findViewById(R.id.emergencyButton);
        locationButton = findViewById(R.id.locationButton);
        disconnectButton = findViewById(R.id.disconnectButton);

        // Set device info
        deviceInfoText.setText(String.format("Device: %s\nAddress: %s", deviceName, deviceAddress));

        // Set up action bar
        getSupportActionBar().setTitle(deviceName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set up button listeners
        emergencyButton.setOnClickListener(v -> sendEmergencySignal());
        locationButton.setOnClickListener(v -> startLocationTracking());
        disconnectButton.setOnClickListener(v -> disconnectDevice());

        // Connect to the device
        connectToDevice();
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    connectionState = BluetoothProfile.STATE_CONNECTED;
                    runOnUiThread(() -> {
                        connectionStatus.setText("Connected");
                        emergencyButton.setEnabled(true);
                        locationButton.setEnabled(true);
                        disconnectButton.setEnabled(true);
                    });
                    bluetoothGatt.discoverServices();
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    connectionState = BluetoothProfile.STATE_DISCONNECTED;
                    runOnUiThread(() -> {
                        connectionStatus.setText("Disconnected");
                        emergencyButton.setEnabled(false);
                        locationButton.setEnabled(false);
                        disconnectButton.setEnabled(false);
                    });
                }
            } else {
                connectionState = BluetoothProfile.STATE_DISCONNECTED;
                runOnUiThread(() -> {
                    connectionStatus.setText("Error: " + status);
                    emergencyButton.setEnabled(false);
                    locationButton.setEnabled(false);
                    disconnectButton.setEnabled(false);
                });
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // Here you would typically set up notifications for the services you're interested in
                setupNotifications();
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            // Handle incoming data from the device
            byte[] data = characteristic.getValue();
            // Process the data according to your device's protocol
        }
    };

    private void connectToDevice() {
        if (deviceAddress == null) {
            Toast.makeText(this, "Device address not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        try {
            BluetoothGatt gatt = bluetoothGatt;
            if (gatt != null) {
                gatt.close();
            }
            bluetoothGatt = BluetoothAdapter.getDefaultAdapter()
                    .getRemoteDevice(deviceAddress)
                    .connectGatt(this, false, gattCallback);
        } catch (Exception e) {
            Toast.makeText(this, "Error connecting to device", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupNotifications() {
        // Set up notifications for the services and characteristics you're interested in
        // This will depend on your specific device's services and characteristics
    }

    private void sendEmergencySignal() {
        // Implement emergency signal sending logic
        Toast.makeText(this, "Sending emergency signal", Toast.LENGTH_SHORT).show();
    }

    private void startLocationTracking() {
        // Implement location tracking logic
        Toast.makeText(this, "Starting location tracking", Toast.LENGTH_SHORT).show();
    }

    private void disconnectDevice() {
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothGatt != null) {
            bluetoothGatt.close();
            bluetoothGatt = null;
        }
    }
} 