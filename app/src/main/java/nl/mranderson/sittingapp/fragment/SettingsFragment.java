package nl.mranderson.sittingapp.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import nl.mranderson.sittingapp.R;
import nl.mranderson.sittingapp.UserPreference;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

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

        Button bSave = (Button) getActivity().findViewById(R.id.bSave);
        CheckBox cSensors = (CheckBox) getActivity().findViewById(R.id.sensors);
        CheckBox cVibration = (CheckBox) getActivity().findViewById(R.id.notify_vibration);
        CheckBox cLight = (CheckBox) getActivity().findViewById(R.id.notify_light);
        CheckBox cSound = (CheckBox) getActivity().findViewById(R.id.notify_sound);
        cSensors.setOnCheckedChangeListener(this);
        cLight.setOnCheckedChangeListener(this);
        cSound.setOnCheckedChangeListener(this);
        cVibration.setOnCheckedChangeListener(this);
        bSave.setOnClickListener(this);

        SharedPreferences prefs = getActivity().getSharedPreferences(UserPreference.MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        Boolean light = prefs.getBoolean("light", true);
        Boolean vibration = prefs.getBoolean("vibration", true);
        Boolean sound = prefs.getBoolean("sound", true);
        Boolean sensors = prefs.getBoolean("sensor", true);

        cLight.setChecked(light);
        cSound.setChecked(sound);
        cVibration.setChecked(vibration);
        cSensors.setChecked(sensors);
    }

    @Override
    public void onClick(View v) {
        // Replace fragment
        Fragment newFragment = new MainFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, newFragment);
        transaction.commit();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()){
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
                UserPreference.setSensorSettings(getActivity(), isChecked);
                break;
        }

    }
}
