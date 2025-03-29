package com.bandapp.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.bandapp.database.entities.EmergencyContact;
import com.bandapp.database.repository.AppRepository;
import java.util.List;

public class EmergencyContactViewModel extends AndroidViewModel {
    private final AppRepository repository;
    private final LiveData<List<EmergencyContact>> contacts;

    public EmergencyContactViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
        contacts = repository.getContactsByUserId(1); // TODO: Get actual user ID
    }

    public LiveData<List<EmergencyContact>> getContacts() {
        return contacts;
    }

    public void insertContact(EmergencyContact contact) {
        repository.insertContact(contact);
    }

    public void updateContact(EmergencyContact contact) {
        repository.updateContact(contact);
    }

    public LiveData<List<EmergencyContact>> getContactsByUserId(int userId) {
        return repository.getContactsByUserId(userId);
    }
} 