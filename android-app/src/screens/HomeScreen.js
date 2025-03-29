import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, FlatList, Alert } from 'react-native';
import { BleManager } from 'react-native-ble-plx';

// Create a single instance of BleManager outside the component
const bleManager = new BleManager();

export default function HomeScreen({ navigation }) {
  const [isScanning, setIsScanning] = useState(false);
  const [devices, setDevices] = useState([]);
  const [isBluetoothReady, setIsBluetoothReady] = useState(false);

  useEffect(() => {
    setupBLE();
    return () => {
      // Cleanup when component unmounts
      if (isScanning) {
        stopScanning();
      }
    };
  }, []);

  const setupBLE = async () => {
    try {
      // Check if Bluetooth is enabled
      const isEnabled = await bleManager.state();
      if (isEnabled !== 'PoweredOn') {
        Alert.alert(
          'Bluetooth Required',
          'Please enable Bluetooth to use this app',
          [{ text: 'OK' }]
        );
        return;
      }

      setIsBluetoothReady(true);
    } catch (error) {
      console.error('Error setting up BLE:', error);
      Alert.alert('Error', 'Failed to initialize Bluetooth');
    }
  };

  const startScanning = async () => {
    if (!isBluetoothReady) {
      Alert.alert('Error', 'Bluetooth is not ready');
      return;
    }

    try {
      setIsScanning(true);
      setDevices([]);

      bleManager.startDeviceScan(null, null, (error, device) => {
        if (error) {
          console.error('Scan error:', error);
          return;
        }

        if (device) {
          setDevices(prevDevices => {
            if (!prevDevices.find(d => d.id === device.id)) {
              return [...prevDevices, device];
            }
            return prevDevices;
          });
        }
      });
    } catch (error) {
      console.error('Error starting scan:', error);
      Alert.alert('Error', 'Failed to start scanning');
      setIsScanning(false);
    }
  };

  const stopScanning = () => {
    try {
      bleManager.stopDeviceScan();
      setIsScanning(false);
    } catch (error) {
      console.error('Error stopping scan:', error);
    }
  };

  const connectToDevice = async (device) => {
    try {
      stopScanning();
      await device.connect();
      navigation.navigate('DeviceControl', { device });
    } catch (error) {
      console.error('Error connecting to device:', error);
      Alert.alert('Error', 'Failed to connect to device');
    }
  };

  const renderDevice = ({ item }) => (
    <TouchableOpacity
      style={styles.deviceItem}
      onPress={() => connectToDevice(item)}
    >
      <Text style={styles.deviceName}>{item.name || 'Unknown Device'}</Text>
      <Text style={styles.deviceId}>{item.id}</Text>
    </TouchableOpacity>
  );

  return (
    <View style={styles.container}>
      <TouchableOpacity
        style={[styles.button, isScanning && styles.buttonActive]}
        onPress={isScanning ? stopScanning : startScanning}
        disabled={!isBluetoothReady}
      >
        <Text style={styles.buttonText}>
          {isScanning ? 'Stop Scan' : 'Start Scan'}
        </Text>
      </TouchableOpacity>

      <FlatList
        data={devices}
        renderItem={renderDevice}
        keyExtractor={item => item.id}
        style={styles.list}
        ListEmptyComponent={
          <Text style={styles.emptyText}>
            {isScanning ? 'Scanning for devices...' : 'No devices found'}
          </Text>
        }
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
    backgroundColor: '#fff',
  },
  button: {
    backgroundColor: '#007AFF',
    padding: 15,
    borderRadius: 8,
    alignItems: 'center',
    marginBottom: 16,
  },
  buttonActive: {
    backgroundColor: '#FF3B30',
  },
  buttonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
  list: {
    flex: 1,
  },
  deviceItem: {
    padding: 16,
    borderBottomWidth: 1,
    borderBottomColor: '#E5E5EA',
  },
  deviceName: {
    fontSize: 16,
    fontWeight: '600',
  },
  deviceId: {
    fontSize: 14,
    color: '#8E8E93',
    marginTop: 4,
  },
  emptyText: {
    textAlign: 'center',
    color: '#8E8E93',
    marginTop: 20,
  },
}); 