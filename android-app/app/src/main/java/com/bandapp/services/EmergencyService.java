package com.bandapp.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.bandapp.R;
import com.bandapp.database.entity.EmergencyContact;
import com.bandapp.database.entity.EmergencyEvent;
import com.bandapp.database.repository.AppRepository;
import java.util.List;

public class EmergencyService extends Service {
    private static final String CHANNEL_ID = "emergency_service_channel";
    private static final int NOTIFICATION_ID = 2;
    private AppRepository repository;
    private long userId;
    private long eventId;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        repository = new AppRepository(getApplication());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            userId = intent.getLongExtra("userId", -1);
            if (userId != -1) {
                startEmergencyEvent();
            }
        }
        return START_STICKY;
    }

    private void startEmergencyEvent() {
        // Create emergency event
        EmergencyEvent event = new EmergencyEvent();
        event.setUserId(userId);
        event.setStatus("ACTIVE");
        event.setTimestamp(System.currentTimeMillis());
        eventId = repository.insertEmergencyEvent(event);

        // Start foreground service with emergency notification
        startForeground(NOTIFICATION_ID, createEmergencyNotification());

        // Notify emergency contacts
        notifyEmergencyContacts();
    }

    private void notifyEmergencyContacts() {
        List<EmergencyContact> contacts = repository.getEmergencyContacts(userId).getValue();
        if (contacts != null) {
            for (EmergencyContact contact : contacts) {
                // TODO: Implement SMS/email notifications
                // For now, just log the contact
                android.util.Log.d("EmergencyService", "Notifying contact: " + contact.getName());
            }
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Emergency Service",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Emergency alerts and notifications");
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setLightColor(android.graphics.Color.RED);
            
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private Notification createEmergencyNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.emergency_active))
                .setContentText(getString(R.string.emergency_status))
                .setSmallIcon(R.drawable.ic_emergency)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 500, 200, 500})
                .setLights(android.graphics.Color.RED, 3000, 3000)
                .build();
    }

    public void resolveEmergency() {
        repository.updateEmergencyEventStatus(eventId, "RESOLVED");
        stopForeground(true);
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
} 