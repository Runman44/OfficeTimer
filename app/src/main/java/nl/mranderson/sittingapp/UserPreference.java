package nl.mranderson.sittingapp;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by MrAnderson on 23/01/16.
 * <p/>
 * UserPreferences are stored here.
 */
public class UserPreference {

    public static final String MY_PREFS_NAME = "settings";
    static SharedPreferences.Editor editor;

    public static void setLightSettings(Activity activity, boolean isChecked) {
        editor = activity.getSharedPreferences(MY_PREFS_NAME, activity.MODE_PRIVATE).edit();
        editor.putBoolean("light", isChecked);
        editor.commit();
    }

    public static void setSoundSettings(Activity activity, boolean isChecked) {
        editor = activity.getSharedPreferences(MY_PREFS_NAME, activity.MODE_PRIVATE).edit();
        editor.putBoolean("sound", isChecked);
        editor.commit();
    }


    public static void setVibrationSettings(Activity activity, boolean isChecked) {
        editor = activity.getSharedPreferences(MY_PREFS_NAME, activity.MODE_PRIVATE).edit();
        editor.putBoolean("vibration", isChecked);
        editor.commit();
    }

    public static void setSensorSettings(Activity activity, boolean isChecked) {
        editor = activity.getSharedPreferences(MY_PREFS_NAME, activity.MODE_PRIVATE).edit();
        editor.putBoolean("sensor", isChecked);
        editor.commit();
    }

    public static void setIntroShown(Activity activity, boolean isChecked) {
        editor = activity.getSharedPreferences(MY_PREFS_NAME, activity.MODE_PRIVATE).edit();
        editor.putBoolean("introShown", isChecked);
        editor.commit();
    }

    public static void setMusicSettings(Activity activity, String musicString) {
        editor = activity.getSharedPreferences(MY_PREFS_NAME, activity.MODE_PRIVATE).edit();
        editor.putString("music", musicString);
        editor.commit();
    }
}
