package nl.mranderson.sittingapp.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.mranderson.sittingapp.R;
import nl.mranderson.sittingapp.UserPreference;
import nl.mranderson.sittingapp.Utils;

public class SettingsFragment extends android.support.v4.app.Fragment implements CompoundButton.OnCheckedChangeListener {


    public static final int REQUEST_TONE = 5;
    private String tone;


    @BindView(R.id.notify_tone)
    TextView cTone;
    @BindView(R.id.sensors)
    CheckBox cSensors;
    @BindView(R.id.notify_light)
    CheckBox cLight;
    @BindView(R.id.notify_sound)
    CheckBox cSound;
    @BindView(R.id.notify_vibration)
    CheckBox cVibration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cSensors.setOnCheckedChangeListener(this);
        cLight.setOnCheckedChangeListener(this);
        cSound.setOnCheckedChangeListener(this);
        cVibration.setOnCheckedChangeListener(this);

        Boolean sensors = UserPreference.getSensorSettings(getActivity());
        Boolean light = UserPreference.getLightSettings(getActivity());
        Boolean vibration = UserPreference.getVibrationSettings(getActivity());
        Boolean sound = UserPreference.getSoundSettings(getActivity());
        tone = UserPreference.getToneSettings(getActivity());

        setRingToneText(tone);

        cLight.setChecked(light);
        cSound.setChecked(sound);
        cVibration.setChecked(vibration);
        cSensors.setChecked(sensors);
    }

    private void setRingToneText(String tone) {
        Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), Uri.parse(tone));
        String title = ringtone.getTitle(getActivity());
        cTone.setText(title);
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
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_TONE) {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

            if (uri != null) {
                UserPreference.setToneSettings(getActivity(), uri.toString());
                tone = uri.toString();

                setRingToneText(tone);
            } else {
                UserPreference.setToneSettings(getActivity(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString());
                tone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString();

                setRingToneText(tone);
            }
        }
    }

    @OnClick(R.id.notify_tone)
    public void onToneClicked(View v) {
        switch (v.getId()) {
            case R.id.notify_tone:
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(tone));
                this.startActivityForResult(intent, REQUEST_TONE);
                break;
        }
    }
}
