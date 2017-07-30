package nl.mranderson.sittingapp.settings;

import android.app.Activity;
import android.content.Intent;
import android.media.RingtoneManager;

import nl.mranderson.sittingapp.common.Navigation;

class SettingsFragmentNavigation implements SettingsNavigation, Navigation {

    private static final int REQUEST_TONE = 5;
    private final Activity context;

    public SettingsFragmentNavigation(Activity activity) {
        this.context = activity;
    }

    @Override
    public void startToneSelection() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
//        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(tone));
        context.startActivityForResult(intent, REQUEST_TONE);
    }
}
