import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Alert } from 'react-native';

export default function DeviceControlScreen({ route, navigation }) {
  const { device } = route.params;
  const [isConnected, setIsConnected] = useState(false);

  useEffect(() => {
    connectToDevice();
    return () => {
      // Cleanup when component unmounts
      if (isConnected) {
        disconnectDevice();
      }
    };
  }, []);

  const connectToDevice = async () => {
    try {
      await device.connect();
      setIsConnected(true);
      Alert.alert('Success', 'Connected to device');
    } catch (error) {
      console.error('Connection error:', error);
      Alert.alert('Error', 'Failed to connect to device');
    }
  };

  const disconnectDevice = async () => {
    try {
      await device.cancelConnection();
      setIsConnected(false);
    } catch (error) {
      console.error('Disconnection error:', error);
    }
  };

  const handleEmergencySignal = () => {
    Alert.alert('Emergency Signal', 'Sending emergency signal...');
    // Implement emergency signal logic here
  };

  const handleLocationTracking = () => {
    Alert.alert('Location Tracking', 'Starting location tracking...');
    // Implement location tracking logic here
  };

  return (
    <View style={styles.container}>
      <View style={styles.statusContainer}>
        <Text style={styles.statusText}>
          Status: {isConnected ? 'Connected' : 'Disconnected'}
        </Text>
        <Text style={styles.deviceName}>{device.name || 'Unknown Device'}</Text>
        <Text style={styles.deviceId}>{device.id}</Text>
      </View>

      <TouchableOpacity
        style={[styles.button, styles.emergencyButton]}
        onPress={handleEmergencySignal}
        disabled={!isConnected}
      >
        <Text style={styles.buttonText}>Emergency Signal</Text>
      </TouchableOpacity>

      <TouchableOpacity
        style={[styles.button, styles.locationButton]}
        onPress={handleLocationTracking}
        disabled={!isConnected}
      >
        <Text style={styles.buttonText}>Start Location Tracking</Text>
      </TouchableOpacity>

      <TouchableOpacity
        style={[styles.button, styles.disconnectButton]}
        onPress={disconnectDevice}
        disabled={!isConnected}
      >
        <Text style={styles.buttonText}>Disconnect</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
    backgroundColor: '#fff',
  },
  statusContainer: {
    backgroundColor: '#f5f5f5',
    padding: 16,
    borderRadius: 8,
    marginBottom: 24,
  },
  statusText: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 8,
  },
  deviceName: {
    fontSize: 16,
    marginBottom: 4,
  },
  deviceId: {
    fontSize: 14,
    color: '#666',
  },
  button: {
    padding: 16,
    borderRadius: 8,
    alignItems: 'center',
    marginBottom: 16,
  },
  emergencyButton: {
    backgroundColor: '#FF3B30',
  },
  locationButton: {
    backgroundColor: '#34C759',
  },
  disconnectButton: {
    backgroundColor: '#8E8E93',
  },
  buttonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
}); 