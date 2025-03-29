package com.bandapp.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.bandapp.database.entity.User;
import com.bandapp.database.repository.AppRepository;

public class UserViewModel extends AndroidViewModel {
    private final AppRepository repository;
    private final MutableLiveData<User> currentUser;

    public UserViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
        currentUser = new MutableLiveData<>();
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public void loadUser(long userId) {
        repository.getUserById(userId).observeForever(currentUser::setValue);
    }

    public void createUser(User user) {
        repository.insertUser(user);
        currentUser.setValue(user);
    }

    public void updateUser(User user) {
        repository.updateUser(user);
        currentUser.setValue(user);
    }

    public void deleteUser(User user) {
        repository.deleteUser(user);
        currentUser.setValue(null);
    }

    public void updateUserStatus(String status) {
        User user = currentUser.getValue();
        if (user != null) {
            user.setStatus(status);
            updateUser(user);
        }
    }

    public void updateUserLocation(double latitude, double longitude) {
        User user = currentUser.getValue();
        if (user != null) {
            user.setLastKnownLatitude(latitude);
            user.setLastKnownLongitude(longitude);
            updateUser(user);
        }
    }

    public void updateUserProfile(String name, String email, String phone) {
        User user = currentUser.getValue();
        if (user != null) {
            user.setName(name);
            user.setEmail(email);
            user.setPhoneNumber(phone);
            repository.updateUser(user);
        }
    }

    public void updateEmergencyPreferences(boolean shareLocation, boolean shareHealthData) {
        User user = currentUser.getValue();
        if (user != null) {
            user.setShareLocation(shareLocation);
            user.setShareHealthData(shareHealthData);
            repository.updateUser(user);
        }
    }

    public LiveData<User> getUserById(int userId) {
        return repository.getUserById(userId);
    }
} 