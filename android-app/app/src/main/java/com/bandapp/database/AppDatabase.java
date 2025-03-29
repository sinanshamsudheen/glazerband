package com.bandapp.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.bandapp.database.dao.DeviceSettingsDao;
import com.bandapp.database.dao.EmergencyContactDao;
import com.bandapp.database.dao.EmergencyEventDao;
import com.bandapp.database.dao.LocationDao;
import com.bandapp.database.dao.UserDao;
import com.bandapp.database.entities.DeviceSettings;
import com.bandapp.database.entities.EmergencyContact;
import com.bandapp.database.entities.EmergencyEvent;
import com.bandapp.database.entities.Location;
import com.bandapp.database.entities.User;

@Database(entities = {
        User.class,
        EmergencyContact.class,
        Location.class,
        EmergencyEvent.class,
        DeviceSettings.class
    },
    version = 1,
    exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    // Define all DAOs
    public abstract UserDao userDao();
    public abstract EmergencyContactDao emergencyContactDao();
    public abstract LocationDao locationDao();
    public abstract EmergencyEventDao emergencyEventDao();
    public abstract DeviceSettingsDao deviceSettingsDao();

    // Type converters for complex types
    public static class Converters {
        // Add type converters if needed
    }

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "bandapp_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
} 