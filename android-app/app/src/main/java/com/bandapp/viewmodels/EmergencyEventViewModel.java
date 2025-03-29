package com.bandapp.viewmodels;

import android.app.Application;
import android.content.Intent;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.bandapp.database.entity.EmergencyContact;
import com.bandapp.database.entity.EmergencyEvent;
import com.bandapp.database.repository.AppRepository;
import com.bandapp.services.EmergencyService;
import java.util.List;

public class EmergencyEventViewModel extends AndroidViewModel {
    private final AppRepository repository;
    private final MutableLiveData<List<EmergencyContact>> contacts;
    private final MutableLiveData<List<EmergencyEvent>> activeEvents;
    private final MutableLiveData<Boolean> isEmergencyActive;

    public EmergencyEventViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
        contacts = new MutableLiveData<>();
        activeEvents = new MutableLiveData<>();
        isEmergencyActive = new MutableLiveData<>(false);
    }

    public LiveData<List<EmergencyContact>> getContacts() {
        return contacts;
    }

    public LiveData<List<EmergencyEvent>> getActiveEvents() {
        return activeEvents;
    }

    public LiveData<Boolean> isEmergencyActive() {
        return isEmergencyActive;
    }

    public void loadContacts(long userId) {
        repository.getEmergencyContacts(userId).observeForever(contacts::setValue);
    }

    public void loadActiveEvents(long userId) {
        repository.getActiveEmergencyEvents(userId).observeForever(events -> {
            activeEvents.setValue(events);
            isEmergencyActive.setValue(!events.isEmpty());
        });
    }

    public void triggerEmergency(long userId) {
        Intent serviceIntent = new Intent(getApplication(), EmergencyService.class);
        serviceIntent.putExtra("userId", userId);
        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            getApplication().startForegroundService(serviceIntent);
        } else {
            getApplication().startService(serviceIntent);
        }
        
        isEmergencyActive.setValue(true);
        loadActiveEvents(userId);
    }

    public void resolveEmergency() {
        Intent serviceIntent = new Intent(getApplication(), EmergencyService.class);
        getApplication().stopService(serviceIntent);
        isEmergencyActive.setValue(false);
    }

    public void addContact(EmergencyContact contact) {
        repository.insertEmergencyContact(contact);
    }

    public void updateContact(EmergencyContact contact) {
        repository.updateEmergencyContact(contact);
    }

    public void deleteContact(EmergencyContact contact) {
        repository.deleteEmergencyContact(contact);
    }
} 