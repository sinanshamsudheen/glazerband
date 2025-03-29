package com.bandapp.viewmodels;

import android.app.Application;
import android.content.Intent;
import android.location.Location;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.bandapp.R;
import com.bandapp.database.entity.Location;
import com.bandapp.database.repository.AppRepository;
import com.bandapp.services.LocationService;
import java.util.List;

public class LocationViewModel extends AndroidViewModel {
    private final AppRepository repository;
    private final MutableLiveData<Location> latestLocation;
    private final MutableLiveData<List<Location>> locationHistory;
    private final MutableLiveData<Boolean> isTracking;

    public LocationViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
        latestLocation = new MutableLiveData<>();
        locationHistory = new MutableLiveData<>();
        isTracking = new MutableLiveData<>(false);
    }

    public LiveData<Location> getLatestLocation() {
        return latestLocation;
    }

    public LiveData<List<Location>> getLocationHistory() {
        return locationHistory;
    }

    public LiveData<Boolean> isTracking() {
        return isTracking;
    }

    public void startTracking(long userId) {
        Intent serviceIntent = new Intent(getApplication(), LocationService.class);
        serviceIntent.putExtra("userId", userId);
        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            getApplication().startForegroundService(serviceIntent);
        } else {
            getApplication().startService(serviceIntent);
        }
        
        isTracking.setValue(true);
        loadLocationHistory(userId);
    }

    public void stopTracking() {
        getApplication().stopService(new Intent(getApplication(), LocationService.class));
        isTracking.setValue(false);
    }

    public void loadLocationHistory(long userId) {
        repository.getLocationsByUserId(userId).observeForever(locationHistory::setValue);
    }

    public void updateLocation(Location location) {
        Location locationEntity = new Location();
        locationEntity.setLatitude(location.getLatitude());
        locationEntity.setLongitude(location.getLongitude());
        locationEntity.setAccuracy(location.getAccuracy());
        locationEntity.setTimestamp(System.currentTimeMillis());
        // TODO: Set user ID
        repository.insertLocation(locationEntity);
        latestLocation.setValue(location);
    }

    public void clearLocationHistory() {
        // TODO: Implement clear history
    }
} 