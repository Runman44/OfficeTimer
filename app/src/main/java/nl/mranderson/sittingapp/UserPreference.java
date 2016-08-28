package nl.mranderson.sittingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.preference.PreferenceManager;

import nl.mranderson.sittingapp.events.TimerState;

/**
 * Created by MrAnderson on 23/01/16.
 * <p/>
 * UserPreferences are stored here.
 */
public class UserPreference {

    public static final String MY_PREFS_NAME = "settings";
    static SharedPreferences.Editor editor;

    public static void setLightSettings(Context context, boolean isChecked) {
        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("light", isChecked);
        editor.commit();
    }

    public static void setSoundSettings(Context context, boolean isChecked) {
        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("sound", isChecked);
        editor.commit();
    }


    public static void setVibrationSettings(Context context, boolean isChecked) {
        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("vibration", isChecked);
        editor.commit();
    }

    public static void setSensorSettings(Context context, boolean isChecked) {
        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("sensor", isChecked);
        editor.commit();
    }

    public static void setIntroShown(Context context, boolean isChecked) {
        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("isFirstStart", isChecked);
        editor.commit();
    }

    public static void setToneSettings(Context context, String musicString) {
        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("music", musicString);
        editor.commit();
    }

    public static void setTimerStatus(Context context, String status) {
        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("timerStatus", status);
        editor.commit();
    }

    public static void setUserWalked(Context context, boolean status) {
        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("userWalked", status);
        editor.commit();
    }

    public static void setCounterTime(Context context, int time) {
        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt("counterTime", time);
        editor.commit();
    }

    public static boolean getLightSettings(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getBoolean("light", false);
    }

    public static boolean getSoundSettings(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getBoolean("sound", false);
    }

    public static boolean getVibrationSettings(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getBoolean("vibration", false);
    }

    public static boolean getSensorSettings(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getBoolean("sensor", false);
    }

    public static boolean getIntroShown(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getBoolean("isFirstStart", false);
    }

    public static String getToneSettings(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getString("music", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString());
    }

    public static String getTimerStatus(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getString("timerStatus", TimerState.PENDING.toString());
    }

    public static boolean getUserWalked(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getBoolean("userWalked", false);
    }

    public static int getCounterTime(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getInt("counterTime", 5);
    }
}
