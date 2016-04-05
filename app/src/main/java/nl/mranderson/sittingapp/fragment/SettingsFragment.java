package nl.mranderson.sittingapp.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import nl.mranderson.sittingapp.R;
import nl.mranderson.sittingapp.UserPreference;
import nl.mranderson.sittingapp.Utils;

public class SettingsFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private CheckBox cSensors;
    private CheckBox cBackground;

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

        ImageButton bSave = (ImageButton) getActivity().findViewById(R.id.bSave);
        cSensors = (CheckBox) getActivity().findViewById(R.id.sensors);
        CheckBox cVibration = (CheckBox) getActivity().findViewById(R.id.notify_vibration);
        CheckBox cLight = (CheckBox) getActivity().findViewById(R.id.notify_light);
        CheckBox cSound = (CheckBox) getActivity().findViewById(R.id.notify_sound);
        cBackground = (CheckBox) getActivity().findViewById(R.id.background);
        cBackground.setOnCheckedChangeListener(this);
        cSensors.setOnCheckedChangeListener(this);
        cLight.setOnCheckedChangeListener(this);
        cSound.setOnCheckedChangeListener(this);
        cVibration.setOnCheckedChangeListener(this);
        bSave.setOnClickListener(this);

        SharedPreferences prefs = getActivity().getSharedPreferences(UserPreference.MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        Boolean background = prefs.getBoolean("background", true);
        Boolean light = prefs.getBoolean("light", true);
        Boolean vibration = prefs.getBoolean("vibration", true);
        Boolean sound = prefs.getBoolean("sound", true);
        Boolean sensors = prefs.getBoolean("sensor", false);

        cLight.setChecked(light);
        cSound.setChecked(sound);
        cVibration.setChecked(vibration);
        cBackground.setChecked(background);
        cSensors.setChecked(sensors);
    }

    @Override
    public void onClick(View v) {
        getActivity().onBackPressed();
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
            case R.id.background:
                UserPreference.setBackgroundSettings(getActivity(), isChecked);
                break;
            case R.id.sensors:
                if (Utils.isPlayServiceAvailable(getActivity())) {
                    UserPreference.setSensorSettings(getActivity(), isChecked);

                    if (!isChecked) {
                        cBackground.setChecked(false);
                        cBackground.setEnabled(false);
                    } else {
                        cBackground.setEnabled(true);
                    }
                    
                } else {
                    PlayServiceAlertDialog dialog = new PlayServiceAlertDialog(getActivity());
                    dialog.setTitle(getResources().getString(R.string.playServiceTitle));
                    dialog.setMessage(getResources().getString(R.string.playServiceMessage));
                    dialog.show();
                    cSensors.setChecked(false);
                    UserPreference.setSensorSettings(getActivity(), false);
                }

                break;
        }
    }
}
