package nl.mranderson.sittingapp.settings;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import nl.mranderson.sittingapp.R;
import nl.mranderson.sittingapp.common.UserPreference;
import nl.mranderson.sittingapp.common.Utils;

public class SettingsFragment extends Fragment implements SettingsContract.View {

    private static final int REQUEST_TONE = 5;
    private TextView cTone;
    private CheckBox cSensors;
    private CheckBox cLight;
    private CheckBox cSound;
    private CheckBox cVibration;
    private CheckBox cWake;
    private SettingsContract.Presenter presenter;
    private UserPreference userPreference;
    private SeekBar cSensitivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        cTone = view.findViewById(R.id.notify_tone);
        cSensors = view.findViewById(R.id.sensors);
        cLight = view.findViewById(R.id.notify_light);
        cSound = view.findViewById(R.id.notify_sound);
        cVibration = view.findViewById(R.id.notify_vibration);
        cWake = view.findViewById(R.id.notify_waking);
        cSensitivity = view.findViewById(R.id.sensitivity);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPresenter();
        setListeners();

        setRingToneText(userPreference.getToneSettings());
        cLight.setChecked(userPreference.getLightSettings());
        cSound.setChecked(userPreference.getSoundSettings());
        cVibration.setChecked(userPreference.getVibrationSettings());
        cSensors.setChecked(userPreference.hasSensorSettingsEnabled());
        cWake.setChecked(userPreference.getWakeSettings());
        cSensitivity.setProgress(userPreference.getSensitivity());
    }

    private void setListeners() {
        cSensors.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Utils.isPlayServiceAvailable(getActivity())) {
                    presenter.onSensorTapped(isChecked);
                } else {
                    Dialog errorDialogPlayService = Utils.getErrorDialogPlayService(getActivity());
                    if (errorDialogPlayService != null)
                        errorDialogPlayService.show();
                    cSensors.setChecked(false);
                }
            }
        });

        cSensitivity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //NO-OP
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //NO-OP
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                presenter.onSensitivityChanged(seekBar.getProgress());
            }
        });
        cLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.onLightTapped(isChecked);
            }
        });
        cSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.onSoundTapped(isChecked);
            }
        });
        cVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.onVibrationTapped(isChecked);
            }
        });
        cWake.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.onStayAwakeTapped(isChecked);
            }
        });
        cTone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onToneTapped();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detach();
    }

    private void setPresenter() {
        userPreference = new UserPreference(getActivity());
        SettingsNavigation navigation = new SettingsFragmentNavigation(getActivity());
        presenter = new SettingsPresenter(userPreference, navigation);
        presenter.attach(this);
    }

    @Override
    public void setRingToneText(String tone) {
        Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), Uri.parse(tone));
        if (ringtone != null) {
            String title = ringtone.getTitle(getActivity());
            cTone.setText(title);
        } else {
            cTone.setText("Can't find default tone.");
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_TONE) {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            presenter.onToneSelected(uri);
        }
    }
}
