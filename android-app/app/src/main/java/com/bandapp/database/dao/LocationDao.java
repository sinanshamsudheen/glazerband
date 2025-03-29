package com.bandapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.bandapp.database.entities.Location;
import java.util.List;

@Dao
public interface LocationDao {
    @Insert
    long insert(Location location);

    @Update
    void update(Location location);

    @Delete
    void delete(Location location);

    @Query("SELECT * FROM location_history WHERE userId = :userId ORDER BY timestamp DESC")
    LiveData<List<Location>> getLocationsByUserId(int userId);

    @Query("SELECT * FROM location_history WHERE userId = :userId ORDER BY timestamp DESC LIMIT 1")
    LiveData<Location> getLatestLocation(int userId);

    @Query("SELECT * FROM location_history WHERE locationId = :locationId")
    LiveData<Location> getLocationById(int locationId);

    @Query("DELETE FROM location_history WHERE userId = :userId")
    void deleteAllLocationsForUser(int userId);

    @Query("DELETE FROM location_history WHERE timestamp < :timestamp")
    void deleteOldLocations(long timestamp);
} 