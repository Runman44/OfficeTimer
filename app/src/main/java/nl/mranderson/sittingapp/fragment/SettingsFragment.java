package nl.mranderson.sittingapp.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import nl.mranderson.sittingapp.R;
import nl.mranderson.sittingapp.UserPreference;
import nl.mranderson.sittingapp.Utils;

public class SettingsFragment extends android.support.v4.app.Fragment implements CompoundButton.OnCheckedChangeListener {

    private CheckBox cSensors;
    private String music;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cSensors = (CheckBox) getActivity().findViewById(R.id.sensors);
        CheckBox cVibration = (CheckBox) getActivity().findViewById(R.id.notify_vibration);
        CheckBox cLight = (CheckBox) getActivity().findViewById(R.id.notify_light);
        CheckBox cSound = (CheckBox) getActivity().findViewById(R.id.notify_sound);
        CheckBox cMusic = (CheckBox) getActivity().findViewById(R.id.notify_music);
        cSensors.setOnCheckedChangeListener(this);
        cLight.setOnCheckedChangeListener(this);
        cSound.setOnCheckedChangeListener(this);
        cVibration.setOnCheckedChangeListener(this);
        cMusic.setOnCheckedChangeListener(this);

        SharedPreferences prefs = getActivity().getSharedPreferences(UserPreference.MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        Boolean light = prefs.getBoolean("light", true);
        Boolean vibration = prefs.getBoolean("vibration", true);
        Boolean sound = prefs.getBoolean("sound", true);
        Boolean sensors = prefs.getBoolean("sensor", false);
        music = prefs.getString("music", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString());

        cLight.setChecked(light);
        cSound.setChecked(sound);
        cVibration.setChecked(vibration);
        cSensors.setChecked(sensors);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.notify_light:
                UserPreference.setLightSettings(getActivity(), isChecked);
                break;
            case R.id.notify_sound:
                UserPreference.setSoundSettings(getActivity(), isChecked);
                break;
            case R.id.notify_vibration:
                UserPreference.setVibrationSettings(getActivity(), isChecked);
                break;
            case R.id.sensors:
                if (Utils.isPlayServiceAvailable(getActivity())) {
                    UserPreference.setSensorSettings(getActivity(), isChecked);
                } else {
                    Dialog errorDialogPlayService = Utils.getErrorDialogPlayService(getActivity());
                    if (errorDialogPlayService != null)
                        errorDialogPlayService.show();
                    cSensors.setChecked(false);
                    UserPreference.setSensorSettings(getActivity(), false);
                }
                break;
            case R.id.notify_music:
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(music));
                this.startActivityForResult(intent, 5);
                break;
        }
    }


    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        if (resultCode == Activity.RESULT_OK && requestCode == 5) {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

            if (uri != null) {
                UserPreference.setMusicSettings(getActivity(), uri.toString());
                music = uri.toString();
            } else {
                UserPreference.setMusicSettings(getActivity(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString());
                music = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString();
            }
        }
    }
}
