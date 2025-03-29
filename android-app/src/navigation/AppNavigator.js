import React from 'react';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import HomeScreen from '../screens/HomeScreen';
import DeviceControlScreen from '../screens/DeviceControlScreen';

const Tab = createBottomTabNavigator();
const Stack = createNativeStackNavigator();

function HomeStack() {
  return (
    <Stack.Navigator>
      <Stack.Screen 
        name="Home" 
        component={HomeScreen}
        options={{ title: 'BandApp' }}
      />
      <Stack.Screen 
        name="DeviceControl" 
        component={DeviceControlScreen}
        options={{ title: 'Device Control' }}
      />
    </Stack.Navigator>
  );
}

export default function AppNavigator() {
  return (
    <Tab.Navigator>
      <Tab.Screen 
        name="HomeTab" 
        component={HomeStack}
        options={{ 
          headerShown: false,
          title: 'Home'
        }}
      />
    </Tab.Navigator>
  );
} 