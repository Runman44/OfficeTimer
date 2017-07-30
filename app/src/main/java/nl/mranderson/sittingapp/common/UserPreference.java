package nl.mranderson.sittingapp.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.preference.PreferenceManager;


public class UserPreference {

    private Context context;
    private SharedPreferences.Editor editor;

    public UserPreference(Context context) {
        this.context = context;
    }

    public void setLightSettings(boolean isChecked) {
        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("light", isChecked);
        editor.apply();
    }

    public void setSensitivity(int sensitivity) {
        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt("sensitivity", sensitivity);
        editor.apply();
    }

    public void setSoundSettings(boolean isChecked) {
        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("sound", isChecked);
        editor.apply();
    }

    public void setVibrationSettings(boolean isChecked) {
        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("vibration", isChecked);
        editor.apply();
    }

    public void setSensorSettings(boolean isChecked) {
        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("sensor", isChecked);
        editor.apply();
    }

    public void setToneSettings(String musicString) {
        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("music", musicString);
        editor.apply();
    }

    public void setUserWalked(boolean status) {
        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("userWalked", status);
        editor.apply();
    }

    public void setWakingSettings(boolean isWaking) {
        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("waking", isWaking);
        editor.apply();
    }

    public int getSensitivity() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getInt("sensitivity", 70);
    }

    public boolean getLightSettings() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getBoolean("light", false);
    }

    public boolean getSoundSettings() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getBoolean("sound", false);
    }

    public boolean getVibrationSettings() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getBoolean("vibration", false);
    }

    public boolean hasSensorSettingsEnabled() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getBoolean("sensor", false);
    }

    public String getToneSettings() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getString("music", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString());
    }

    public boolean hasUserWalkedBefore() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getBoolean("userWalked", false);
    }

    public Boolean getWakeSettings() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getBoolean("waking", false);
    }
}
