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
import com.bandapp.R;
import com.bandapp.viewmodels.UserViewModel;
import com.google.android.material.button.MaterialButton;

public class HomeFragment extends Fragment {
    private UserViewModel userViewModel;
    private TextView userNameText;
    private TextView emergencyStatusText;
    private MaterialButton btnEmergency;
    private MaterialButton btnContacts;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        userNameText = view.findViewById(R.id.user_name);
        emergencyStatusText = view.findViewById(R.id.emergency_status);
        btnEmergency = view.findViewById(R.id.btn_emergency);
        btnContacts = view.findViewById(R.id.btn_contacts);

        // Initialize ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Setup observers
        userViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                userNameText.setText(user.getName());
            }
        });

        // Setup click listeners
        btnEmergency.setOnClickListener(v -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_home_to_emergency);
        });

        btnContacts.setOnClickListener(v -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_home_to_contacts);
        });
    }
} 