import React, { useState } from 'react';
import { View, Text, StyleSheet, Switch, TouchableOpacity, Alert } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useTheme } from '../context/ThemeContext';

export default function SettingsScreen() {
  const { colors, isDarkMode, toggleTheme } = useTheme();
  const [isBatterySaver, setIsBatterySaver] = useState(false);

  const toggleBatterySaver = () => {
    setIsBatterySaver(!isBatterySaver);
    Alert.alert(
      isBatterySaver ? 'Battery Saver Disabled' : 'Battery Saver Enabled',
      isBatterySaver 
        ? 'Battery saver mode has been disabled.' 
        : 'Battery saver mode has been enabled. This will help conserve battery life.',
      [{ text: 'OK' }]
    );
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
        <Text style={styles.title}>Settings</Text>
        <Text style={styles.subtitle}>Customize your app experience</Text>
      </View>

      <View style={styles.settingsContainer}>
        <View style={styles.settingItem}>
          <View style={styles.settingInfo}>
            <Ionicons 
              name={isDarkMode ? "moon" : "sunny"} 
              size={24} 
              color={colors.primary} 
            />
            <Text style={styles.settingText}>Dark Mode</Text>
          </View>
          <Switch
            value={isDarkMode}
            onValueChange={toggleTheme}
            trackColor={{ false: '#767577', true: colors.primary }}
            thumbColor={isDarkMode ? colors.primary : '#f4f3f4'}
          />
        </View>

        <View style={styles.settingItem}>
          <View style={styles.settingInfo}>
            <Ionicons name="battery-charging" size={24} color={colors.primary} />
            <Text style={styles.settingText}>Battery Saver Mode</Text>
          </View>
          <Switch
            value={isBatterySaver}
            onValueChange={toggleBatterySaver}
            trackColor={{ false: '#767577', true: colors.primary }}
            thumbColor={isBatterySaver ? colors.primary : '#f4f3f4'}
          />
        </View>
      </View>
    </View>
  );
} 