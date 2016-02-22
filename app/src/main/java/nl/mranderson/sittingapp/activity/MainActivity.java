package nl.mranderson.sittingapp.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import nl.mranderson.sittingapp.Constants;
import nl.mranderson.sittingapp.R;
import nl.mranderson.sittingapp.UserPreference;
import nl.mranderson.sittingapp.fragment.MainFragment;
import nl.mranderson.sittingapp.fragment.TimerFragment;
import nl.mranderson.sittingapp.fragment.TutorialFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = this.getSharedPreferences(UserPreference.MY_PREFS_NAME, MODE_PRIVATE);
        Boolean introShown = prefs.getBoolean("introShown", false);

        if (Constants.IS_TIMER_SERVICE_RUNNING) {
            TimerFragment fragment = new TimerFragment();
            getFragmentManager().beginTransaction().add(R.id.fragment, fragment, "timer").commit();
        } else {
            if (!introShown) {
                TutorialFragment fragment = new TutorialFragment();
                getFragmentManager().beginTransaction().add(R.id.fragment, fragment, "tutorial").commit();
                UserPreference.setIntroShown(this, true);
            } else if (getFragmentManager().findFragmentByTag("main") == null) {
                MainFragment fragment = new MainFragment();
                getFragmentManager().beginTransaction().add(R.id.fragment, fragment, "main").commit();
            }
        }

    }

    public void onStartMain(View view) {
        Fragment newFragment = new MainFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, newFragment);
        transaction.commit();
    }
}
