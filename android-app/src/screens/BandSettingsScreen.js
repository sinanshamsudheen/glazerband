import React, { useState } from 'react';
import { View, Text, StyleSheet, Switch, TouchableOpacity, Alert, ActivityIndicator } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useTheme } from '../context/ThemeContext';

export default function BandSettingsScreen() {
  const { colors } = useTheme();
  const [isConnected, setIsConnected] = useState(false);
  const [autoConnect, setAutoConnect] = useState(true);
  const [batterySaver, setBatterySaver] = useState(false);
  const [isScanning, setIsScanning] = useState(false);

  const handleConnect = () => {
    setIsScanning(true);
    // Simulate connection process
    setTimeout(() => {
      setIsScanning(false);
      setIsConnected(!isConnected);
      Alert.alert(
        isConnected ? 'Disconnected' : 'Connected',
        isConnected ? 'Your band has been disconnected.' : 'Successfully connected to your band.',
        [{ text: 'OK' }]
      );
    }, 2000);
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
    connectButton: {
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'center',
      backgroundColor: colors.card,
      padding: 15,
      borderRadius: 10,
      marginBottom: 30,
    },
    connectedButton: {
      backgroundColor: colors.primary,
    },
    connectButtonText: {
      marginLeft: 10,
      fontSize: 18,
      color: colors.primary,
    },
    connectedButtonText: {
      color: '#FFFFFF',
    },
    settingsContainer: {
      backgroundColor: colors.card,
      borderRadius: 10,
      padding: 15,
    },
    settingItem: {
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'space-between',
      paddingVertical: 15,
      borderBottomWidth: 1,
      borderBottomColor: colors.border,
    },
    settingInfo: {
      flexDirection: 'row',
      alignItems: 'center',
    },
    settingText: {
      marginLeft: 10,
      fontSize: 16,
      color: colors.text,
    },
  });

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.title}>Band Settings</Text>
        <Text style={styles.subtitle}>Manage your band connection and preferences</Text>
      </View>

      <TouchableOpacity 
        style={[styles.connectButton, isConnected && styles.connectedButton]}
        onPress={handleConnect}
        disabled={isScanning}
      >
        {isScanning ? (
          <ActivityIndicator color={isConnected ? "#FFFFFF" : colors.primary} />
        ) : (
          <>
            <Ionicons 
              name={isConnected ? "bluetooth" : "bluetooth-outline"} 
              size={24} 
              color={isConnected ? "#FFFFFF" : colors.primary} 
            />
            <Text style={[styles.connectButtonText, isConnected && styles.connectedButtonText]}>
              {isConnected ? 'Disconnect' : 'Connect Band'}
            </Text>
          </>
        )}
      </TouchableOpacity>

      <View style={styles.settingsContainer}>
        <View style={styles.settingItem}>
          <View style={styles.settingInfo}>
            <Ionicons name="bluetooth" size={24} color={colors.primary} />
            <Text style={styles.settingText}>Auto-connect</Text>
          </View>
          <Switch
            value={autoConnect}
            onValueChange={setAutoConnect}
            trackColor={{ false: '#767577', true: colors.primary }}
            thumbColor={autoConnect ? colors.primary : '#f4f3f4'}
          />
        </View>

        <View style={styles.settingItem}>
          <View style={styles.settingInfo}>
            <Ionicons name="battery-charging" size={24} color={colors.primary} />
            <Text style={styles.settingText}>Battery Saver Mode</Text>
          </View>
          <Switch
            value={batterySaver}
            onValueChange={setBatterySaver}
            trackColor={{ false: '#767577', true: colors.primary }}
            thumbColor={batterySaver ? colors.primary : '#f4f3f4'}
          />
        </View>
      </View>
    </View>
  );
} 