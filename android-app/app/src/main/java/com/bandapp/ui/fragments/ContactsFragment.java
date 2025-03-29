package com.bandapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bandapp.R;
import com.bandapp.database.entity.EmergencyContact;
import com.bandapp.ui.adapters.EmergencyContactsAdapter;
import com.bandapp.viewmodels.EmergencyEventViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class ContactsFragment extends Fragment implements EmergencyContactsAdapter.OnContactClickListener {
    private EmergencyEventViewModel viewModel;
    private RecyclerView contactsRecyclerView;
    private ExtendedFloatingActionButton fabAddContact;
    private EmergencyContactsAdapter contactsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        contactsRecyclerView = view.findViewById(R.id.contacts_recycler_view);
        fabAddContact = view.findViewById(R.id.fab_add_contact);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(EmergencyEventViewModel.class);

        // Setup RecyclerView
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        contactsAdapter = new EmergencyContactsAdapter(this);
        contactsRecyclerView.setAdapter(contactsAdapter);

        // Setup observers
        viewModel.getEmergencyContacts().observe(getViewLifecycleOwner(), contacts -> {
            contactsAdapter.submitList(contacts);
        });

        // Setup click listeners
        fabAddContact.setOnClickListener(v -> {
            showAddContactDialog();
        });
    }

    @Override
    public void onContactClick(EmergencyContact contact) {
        showEditContactDialog(contact);
    }

    private void showAddContactDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_contact, null);
        TextInputEditText nameEdit = dialogView.findViewById(R.id.edit_text_name);
        TextInputEditText phoneEdit = dialogView.findViewById(R.id.edit_text_phone);
        TextInputEditText relationshipEdit = dialogView.findViewById(R.id.edit_text_relationship);

        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add Emergency Contact")
            .setView(dialogView)
            .setPositiveButton("Add", (dialog, which) -> {
                String name = nameEdit.getText().toString();
                String phone = phoneEdit.getText().toString();
                String relationship = relationshipEdit.getText().toString();

                if (!name.isEmpty() && !phone.isEmpty() && !relationship.isEmpty()) {
                    EmergencyContact contact = new EmergencyContact();
                    contact.setUserId(1); // TODO: Get actual user ID
                    contact.setName(name);
                    contact.setPhoneNumber(phone);
                    contact.setRelationship(relationship);
                    viewModel.addEmergencyContact(contact);
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void showEditContactDialog(EmergencyContact contact) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_contact, null);
        TextInputEditText nameEdit = dialogView.findViewById(R.id.edit_text_name);
        TextInputEditText phoneEdit = dialogView.findViewById(R.id.edit_text_phone);
        TextInputEditText relationshipEdit = dialogView.findViewById(R.id.edit_text_relationship);

        // Pre-fill the fields
        nameEdit.setText(contact.getName());
        phoneEdit.setText(contact.getPhoneNumber());
        relationshipEdit.setText(contact.getRelationship());

        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("Edit Emergency Contact")
            .setView(dialogView)
            .setPositiveButton("Save", (dialog, which) -> {
                String name = nameEdit.getText().toString();
                String phone = phoneEdit.getText().toString();
                String relationship = relationshipEdit.getText().toString();

                if (!name.isEmpty() && !phone.isEmpty() && !relationship.isEmpty()) {
                    contact.setName(name);
                    contact.setPhoneNumber(phone);
                    contact.setRelationship(relationship);
                    viewModel.updateEmergencyContact(contact);
                }
            })
            .setNegativeButton("Cancel", null)
            .setNeutralButton("Delete", (dialog, which) -> {
                viewModel.deleteEmergencyContact(contact);
            })
            .show();
    }
} 