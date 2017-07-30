package nl.mranderson.sittingapp.settings;


import android.media.RingtoneManager;
import android.net.Uri;

import nl.mranderson.sittingapp.common.UserPreference;

public class SettingsPresenter implements SettingsContract.Presenter {

    private SettingsContract.View view;
    private UserPreference userPreference;
    private SettingsNavigation navigation;

    public SettingsPresenter(UserPreference userPreference, SettingsNavigation navigation) {
        this.userPreference = userPreference;
        this.navigation = navigation;
    }

    @Override
    public void attach(SettingsContract.View view) {
        this.view = view;
    }

    @Override
    public void detach() {
        view = null;
    }

    @Override
    public void onSensorTapped(boolean isChecked) {
        userPreference.setSensorSettings(isChecked);
    }

    @Override
    public void onLightTapped(boolean isChecked) {
        userPreference.setLightSettings(isChecked);
    }

    @Override
    public void onSoundTapped(boolean isChecked) {
        userPreference.setSoundSettings(isChecked);
    }

    @Override
    public void onVibrationTapped(boolean isChecked) {
        userPreference.setVibrationSettings(isChecked);
    }

    @Override
    public void onStayAwakeTapped(boolean isChecked) {
        userPreference.setWakingSettings(isChecked);
    }

    @Override
    public void onToneTapped() {
        navigation.startToneSelection();
    }

    @Override
    public void onToneSelected(Uri uri) {
        if (uri != null) {
            userPreference.setToneSettings(uri.toString());
            view.setRingToneText(uri.toString());
        } else {
            userPreference.setToneSettings(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString());
            view.setRingToneText(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString());
        }
    }
}
