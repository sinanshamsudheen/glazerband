import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Alert, FlatList, TextInput, Linking, ActivityIndicator, ScrollView } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import * as Location from 'expo-location';
import * as SMS from 'expo-sms';
import { useTheme } from '../context/ThemeContext';

export default function EmergencyScreen() {
  const { colors } = useTheme();
  const [contacts, setContacts] = useState([]);
  const [newContact, setNewContact] = useState({ name: '', phone: '' });
  const [isAddingContact, setIsAddingContact] = useState(false);
  const [isSending, setIsSending] = useState(false);

  useEffect(() => {
    loadContacts();
    checkSMSAvailability();
  }, []);

  const checkSMSAvailability = async () => {
    try {
      const isAvailable = await SMS.isAvailableAsync();
      if (!isAvailable) {
        Alert.alert(
          'SMS Not Available',
          'SMS functionality is not available on this device.',
          [{ text: 'OK' }]
        );
      }
    } catch (error) {
      console.error('Error checking SMS availability:', error);
    }
  };

  const loadContacts = async () => {
    try {
      const savedContacts = await AsyncStorage.getItem('emergencyContacts');
      if (savedContacts) {
        setContacts(JSON.parse(savedContacts));
      }
    } catch (error) {
      console.error('Error loading contacts:', error);
    }
  };

  const saveContacts = async (updatedContacts) => {
    try {
      await AsyncStorage.setItem('emergencyContacts', JSON.stringify(updatedContacts));
      setContacts(updatedContacts);
    } catch (error) {
      console.error('Error saving contacts:', error);
    }
  };

  const addContact = () => {
    if (!newContact.name || !newContact.phone) {
      Alert.alert('Error', 'Please fill in all fields');
      return;
    }

    // Basic phone number validation
    const phoneRegex = /^\+?[\d\s-]{10,}$/;
    if (!phoneRegex.test(newContact.phone)) {
      Alert.alert('Error', 'Please enter a valid phone number');
      return;
    }

    const updatedContacts = [...contacts, newContact];
    saveContacts(updatedContacts);
    setNewContact({ name: '', phone: '' });
    setIsAddingContact(false);
  };

  const removeContact = (index) => {
    Alert.alert(
      'Remove Contact',
      'Are you sure you want to remove this contact?',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Remove',
          style: 'destructive',
          onPress: () => {
            const updatedContacts = contacts.filter((_, i) => i !== index);
            saveContacts(updatedContacts);
          },
        },
      ]
    );
  };

  const sendEmergencySMS = async (locationString) => {
    try {
      const phoneNumbers = contacts.map(contact => contact.phone);
      const message = `EMERGENCY ALERT: I need help! My current location is:\n${locationString}`;
      
      await SMS.sendSMSAsync(phoneNumbers, message);
      return true;
    } catch (error) {
      console.error('Error sending SMS:', error);
      return false;
    }
  };

  const handleEmergencyPress = async () => {
    if (contacts.length === 0) {
      Alert.alert(
        'No Contacts',
        'Please add emergency contacts first.',
        [{ text: 'OK' }]
      );
      return;
    }

    Alert.alert(
      'Emergency Alert',
      'Are you sure you want to send an emergency alert?',
      [
        {
          text: 'Cancel',
          style: 'cancel',
        },
        {
          text: 'Send Alert',
          onPress: async () => {
            try {
              setIsSending(true);
              const location = await Location.getCurrentPositionAsync({
                accuracy: Location.Accuracy.High,
              });

              const locationString = 
                `Latitude: ${location.coords.latitude.toFixed(6)}\n` +
                `Longitude: ${location.coords.longitude.toFixed(6)}\n` +
                `Accuracy: ${location.coords.accuracy.toFixed(2)}m`;

              const smsSent = await sendEmergencySMS(locationString);
              
              if (smsSent) {
                Alert.alert(
                  'Alert Sent',
                  'Emergency alert has been sent to your contacts with your location.',
                  [{ text: 'OK' }]
                );
              } else {
                Alert.alert(
                  'Error',
                  'Failed to send emergency alert. Please try again.',
                  [{ text: 'OK' }]
                );
              }
            } catch (error) {
              console.error('Error sending emergency alert:', error);
              Alert.alert(
                'Error',
                'Failed to send emergency alert. Please try again.',
                [{ text: 'OK' }]
              );
            } finally {
              setIsSending(false);
            }
          },
        },
      ]
    );
  };

  const renderContact = ({ item, index }) => (
    <View style={styles.contactItem}>
      <View style={styles.contactInfo}>
        <Ionicons name="person" size={24} color="#007AFF" />
        <View style={styles.contactText}>
          <Text style={styles.contactName}>{item.name}</Text>
          <Text style={styles.contactPhone}>{item.phone}</Text>
        </View>
      </View>
      <TouchableOpacity
        onPress={() => removeContact(index)}
        style={styles.removeButton}
      >
        <Ionicons name="trash-outline" size={24} color="#FF3B30" />
      </TouchableOpacity>
    </View>
  );

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
    emergencyButton: {
      backgroundColor: colors.error,
      padding: 20,
      borderRadius: 15,
      alignItems: 'center',
      marginBottom: 30,
      shadowColor: '#000',
      shadowOffset: {
        width: 0,
        height: 2,
      },
      shadowOpacity: 0.25,
      shadowRadius: 3.84,
      elevation: 5,
    },
    buttonText: {
      fontSize: 24,
      fontWeight: 'bold',
      color: '#FFFFFF',
      marginTop: 10,
    },
    contactsContainer: {
      backgroundColor: colors.card,
      borderRadius: 10,
      padding: 15,
      marginBottom: 20,
    },
    contactItem: {
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'space-between',
      paddingVertical: 10,
      borderBottomWidth: 1,
      borderBottomColor: colors.border,
    },
    contactInfo: {
      flex: 1,
    },
    contactText: {
      marginLeft: 10,
    },
    contactName: {
      fontSize: 16,
      fontWeight: 'bold',
      color: colors.text,
    },
    contactPhone: {
      fontSize: 14,
      color: colors.text,
      opacity: 0.7,
    },
    removeButton: {
      padding: 5,
    },
    inputContainer: {
      flexDirection: 'row',
      marginBottom: 20,
    },
    input: {
      flex: 1,
      backgroundColor: colors.card,
      borderRadius: 8,
      padding: 10,
      marginRight: 10,
      color: colors.text,
    },
    addButton: {
      backgroundColor: colors.primary,
      padding: 10,
      borderRadius: 8,
      justifyContent: 'center',
    },
    addButtonText: {
      color: '#FFFFFF',
      fontWeight: 'bold',
    },
  });

  return (
    <ScrollView style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.title}>Emergency Contacts</Text>
        <Text style={styles.subtitle}>Add trusted contacts for emergency alerts</Text>
      </View>

      <TouchableOpacity 
        style={styles.emergencyButton}
        onPress={handleEmergencyPress}
        disabled={isSending}
      >
        <Ionicons name="warning" size={40} color="#FFFFFF" />
        <Text style={styles.buttonText}>Send Emergency Alert</Text>
      </TouchableOpacity>

      <View style={styles.contactsContainer}>
        <View style={styles.contactsHeader}>
          <Text style={styles.contactsTitle}>Emergency Contacts</Text>
          <TouchableOpacity
            onPress={() => setIsAddingContact(true)}
            style={styles.addButton}
          >
            <Ionicons name="add-circle" size={24} color="#007AFF" />
            <Text style={styles.addButtonText}>Add Contact</Text>
          </TouchableOpacity>
        </View>

        {isAddingContact && (
          <View style={styles.addContactForm}>
            <TextInput
              style={styles.input}
              placeholder="Contact Name"
              value={newContact.name}
              onChangeText={(text) => setNewContact({ ...newContact, name: text })}
            />
            <TextInput
              style={styles.input}
              placeholder="Phone Number"
              value={newContact.phone}
              onChangeText={(text) => setNewContact({ ...newContact, phone: text })}
              keyboardType="phone-pad"
            />
            <View style={styles.formButtons}>
              <TouchableOpacity
                style={[styles.formButton, styles.cancelButton]}
                onPress={() => {
                  setIsAddingContact(false);
                  setNewContact({ name: '', phone: '' });
                }}
              >
                <Text style={styles.cancelButtonText}>Cancel</Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={[styles.formButton, styles.saveButton]}
                onPress={addContact}
              >
                <Text style={styles.saveButtonText}>Save</Text>
              </TouchableOpacity>
            </View>
          </View>
        )}

        <FlatList
          data={contacts}
          renderItem={renderContact}
          keyExtractor={(_, index) => index.toString()}
          style={styles.contactsList}
        />
      </View>

      <View style={styles.inputContainer}>
        <TextInput
          style={styles.input}
          placeholder="Contact Name"
          placeholderTextColor={colors.text + '80'}
          value={newContact.name}
          onChangeText={(text) => setNewContact({ ...newContact, name: text })}
        />
        <TextInput
          style={styles.input}
          placeholder="Phone Number"
          placeholderTextColor={colors.text + '80'}
          value={newContact.phone}
          onChangeText={(text) => setNewContact({ ...newContact, phone: text })}
          keyboardType="phone-pad"
        />
        <TouchableOpacity 
          style={styles.addButton}
          onPress={addContact}
        >
          <Text style={styles.addButtonText}>Add</Text>
        </TouchableOpacity>
      </View>
    </ScrollView>
  );
} 