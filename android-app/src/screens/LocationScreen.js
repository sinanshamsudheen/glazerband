import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Alert, ActivityIndicator } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useTheme } from '../context/ThemeContext';
import * as Location from 'expo-location';

export default function LocationScreen() {
  const { colors } = useTheme();
  const [location, setLocation] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState(null);

  useEffect(() => {
    (async () => {
      let { status } = await Location.requestForegroundPermissionsAsync();
      if (status !== 'granted') {
        setErrorMsg('Permission to access location was denied');
        return;
      }

      let location = await Location.getCurrentPositionAsync({});
      setLocation(location);
    })();
  }, []);

  const getLocation = async () => {
    setIsLoading(true);
    try {
      let location = await Location.getCurrentPositionAsync({});
      setLocation(location);
    } catch (error) {
      setErrorMsg('Error getting location');
    }
    setIsLoading(false);
  };

  const shareLocation = async () => {
    if (!location) {
      Alert.alert('Error', 'Location not available');
      return;
    }

    const message = `My current location: https://www.google.com/maps?q=${location.coords.latitude},${location.coords.longitude}`;
    // Here you would implement sharing functionality
    Alert.alert('Location Shared', 'Your location has been shared successfully');
  };

  const styles = StyleSheet.create({
    container: {
      flex: 1,
      backgroundColor: colors.background,
      padding: 20,
    },
    header: {
      alignItems: 'center',
      marginTop: 40,
      marginBottom: 30,
    },
    title: {
      fontSize: 32,
      fontWeight: 'bold',
      color: colors.primary,
    },
    subtitle: {
      fontSize: 18,
      color: colors.text,
      opacity: 0.7,
      marginTop: 5,
    },
    locationContainer: {
      backgroundColor: colors.card,
      borderRadius: 10,
      padding: 20,
      marginBottom: 20,
    },
    locationText: {
      fontSize: 16,
      color: colors.text,
      marginBottom: 10,
    },
    coordinatesContainer: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      marginTop: 10,
    },
    coordinateItem: {
      flex: 1,
      backgroundColor: colors.background,
      padding: 10,
      borderRadius: 5,
      marginHorizontal: 5,
    },
    coordinateLabel: {
      fontSize: 12,
      color: colors.text,
      opacity: 0.7,
      marginBottom: 5,
    },
    coordinateValue: {
      fontSize: 16,
      color: colors.primary,
      fontWeight: 'bold',
    },
    buttonContainer: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      marginTop: 20,
    },
    button: {
      flex: 1,
      backgroundColor: colors.primary,
      padding: 15,
      borderRadius: 10,
      alignItems: 'center',
      marginHorizontal: 5,
    },
    buttonText: {
      color: '#FFFFFF',
      fontSize: 16,
      fontWeight: 'bold',
    },
    errorText: {
      color: colors.error,
      textAlign: 'center',
      marginTop: 10,
    },
  });

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.title}>Location</Text>
        <Text style={styles.subtitle}>Track your current location</Text>
      </View>

      <View style={styles.locationContainer}>
        <Text style={styles.locationText}>Current Location:</Text>
        {isLoading ? (
          <ActivityIndicator size="large" color={colors.primary} />
        ) : location ? (
          <View style={styles.coordinatesContainer}>
            <View style={styles.coordinateItem}>
              <Text style={styles.coordinateLabel}>Latitude</Text>
              <Text style={styles.coordinateValue}>
                {location.coords.latitude.toFixed(6)}
              </Text>
            </View>
            <View style={styles.coordinateItem}>
              <Text style={styles.coordinateLabel}>Longitude</Text>
              <Text style={styles.coordinateValue}>
                {location.coords.longitude.toFixed(6)}
              </Text>
            </View>
          </View>
        ) : (
          <Text style={styles.errorText}>Location not available</Text>
        )}
      </View>

      <View style={styles.buttonContainer}>
        <TouchableOpacity 
          style={styles.button}
          onPress={getLocation}
          disabled={isLoading}
        >
          <Text style={styles.buttonText}>Update Location</Text>
        </TouchableOpacity>

        <TouchableOpacity 
          style={styles.button}
          onPress={shareLocation}
        >
          <Text style={styles.buttonText}>Share Location</Text>
        </TouchableOpacity>
      </View>

      {errorMsg && (
        <Text style={styles.errorText}>{errorMsg}</Text>
      )}
    </View>
  );
} 