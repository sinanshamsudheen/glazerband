package com.bandapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bandapp.R;
import com.bandapp.ui.adapters.EmergencyContactsAdapter;
import com.bandapp.viewmodels.BluetoothViewModel;
import com.bandapp.viewmodels.EmergencyEventViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class EmergencyFragment extends Fragment {
    private EmergencyEventViewModel emergencyViewModel;
    private BluetoothViewModel bluetoothViewModel;
    private TextView emergencyStatusText;
    private TextView lastTriggeredText;
    private TextView bluetoothStatusText;
    private RecyclerView contactsRecyclerView;
    private ExtendedFloatingActionButton fabTriggerEmergency;
    private EmergencyContactsAdapter contactsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_emergency, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        emergencyStatusText = view.findViewById(R.id.emergency_status);
        lastTriggeredText = view.findViewById(R.id.last_triggered);
        bluetoothStatusText = view.findViewById(R.id.bluetooth_status);
        contactsRecyclerView = view.findViewById(R.id.contacts_recycler_view);
        fabTriggerEmergency = view.findViewById(R.id.fab_trigger_emergency);

        // Initialize ViewModels
        emergencyViewModel = new ViewModelProvider(this).get(EmergencyEventViewModel.class);
        bluetoothViewModel = new ViewModelProvider(this).get(BluetoothViewModel.class);

        // Setup RecyclerView
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        contactsAdapter = new EmergencyContactsAdapter(contact -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_emergency_to_contacts);
        });
        contactsRecyclerView.setAdapter(contactsAdapter);

        // Setup observers
        emergencyViewModel.getContacts().observe(getViewLifecycleOwner(), contacts -> {
            contactsAdapter.submitList(contacts);
        });

        emergencyViewModel.getActiveEvents().observe(getViewLifecycleOwner(), events -> {
            if (events != null && !events.isEmpty()) {
                EmergencyEvent event = events.get(0);
                emergencyStatusText.setText(R.string.emergency_active);
                lastTriggeredText.setText(String.format("Last triggered: %s",
                        new java.text.SimpleDateFormat("MMM dd, yyyy HH:mm")
                                .format(new java.util.Date(event.getTimestamp()))));
                fabTriggerEmergency.setText(R.string.emergency_resolved);
            } else {
                emergencyStatusText.setText(R.string.status_normal);
                lastTriggeredText.setText(R.string.last_triggered);
                fabTriggerEmergency.setText(R.string.trigger_emergency);
            }
        });

        // Setup Bluetooth observers
        bluetoothViewModel.getConnectionStatus().observe(getViewLifecycleOwner(), status -> {
            bluetoothStatusText.setText(status);
        });

        bluetoothViewModel.isConnected().observe(getViewLifecycleOwner(), isConnected -> {
            fabTriggerEmergency.setEnabled(isConnected);
        });

        // Setup click listeners
        fabTriggerEmergency.setOnClickListener(v -> {
            if (emergencyViewModel.isEmergencyActive().getValue() == Boolean.TRUE) {
                emergencyViewModel.resolveEmergency();
                Snackbar.make(requireView(), R.string.emergency_resolved,
                        Snackbar.LENGTH_SHORT).show();
            } else {
                // TODO: Get actual user ID
                emergencyViewModel.triggerEmergency(1);
                bluetoothViewModel.sendEmergencyTrigger();
                Snackbar.make(requireView(), R.string.emergency_active,
                        Snackbar.LENGTH_SHORT).show();
            }
        });

        // Load data
        // TODO: Get actual user ID
        emergencyViewModel.loadContacts(1);
        emergencyViewModel.loadActiveEvents(1);
        bluetoothViewModel.startScan();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bluetoothViewModel.disconnect();
    }
} 