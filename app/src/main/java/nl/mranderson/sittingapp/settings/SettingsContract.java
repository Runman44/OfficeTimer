package nl.mranderson.sittingapp.settings;

import android.net.Uri;

public interface SettingsContract {

    interface View {

        void setRingToneText(String ringTone);
    }

    interface Presenter {

        void attach(View view);

        void detach();

        void onSensorTapped(boolean isChecked);

        void onLightTapped(boolean isChecked);

        void onSoundTapped(boolean isChecked);

        void onVibrationTapped(boolean isChecked);

        void onStayAwakeTapped(boolean isChecked);

        void onToneTapped();

        void onToneSelected(Uri uri);
    }

}
