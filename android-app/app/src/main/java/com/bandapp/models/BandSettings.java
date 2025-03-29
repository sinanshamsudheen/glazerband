package com.bandapp.models;

public class BandSettings {
    private boolean vibrationEnabled;
    private boolean soundEnabled;
    private int vibrationIntensity;
    private int soundVolume;
    private int ledBrightness;

    public BandSettings() {
        // Default settings
        this.vibrationEnabled = true;
        this.soundEnabled = true;
        this.vibrationIntensity = 100;
        this.soundVolume = 100;
        this.ledBrightness = 100;
    }

    public boolean isVibrationEnabled() {
        return vibrationEnabled;
    }

    public void setVibrationEnabled(boolean vibrationEnabled) {
        this.vibrationEnabled = vibrationEnabled;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }

    public int getVibrationIntensity() {
        return vibrationIntensity;
    }

    public void setVibrationIntensity(int vibrationIntensity) {
        this.vibrationIntensity = Math.max(0, Math.min(100, vibrationIntensity));
    }

    public int getSoundVolume() {
        return soundVolume;
    }

    public void setSoundVolume(int soundVolume) {
        this.soundVolume = Math.max(0, Math.min(100, soundVolume));
    }

    public int getLedBrightness() {
        return ledBrightness;
    }

    public void setLedBrightness(int ledBrightness) {
        this.ledBrightness = Math.max(0, Math.min(100, ledBrightness));
    }
} 