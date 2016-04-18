package nl.mranderson.sittingapp.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import co.mobiwise.materialintro.animation.MaterialIntroListener;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.view.MaterialIntroView;
import nl.mranderson.sittingapp.Constants;
import nl.mranderson.sittingapp.MaterialIntroUtils;
import nl.mranderson.sittingapp.R;
import nl.mranderson.sittingapp.UserPreference;
import nl.mranderson.sittingapp.custom.CircularSeekBar;

public class MainFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private CircularSeekBar circularSeekbar;
    private TextView timeText;
    private boolean introShown;
    private Button button;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        button = (Button) getActivity().findViewById(R.id.bStart);
        button.setOnClickListener(this);

        timeText = (TextView) getActivity().findViewById(R.id.timeText);
        timeText.setText(getTimeText(Constants.TIMER_SELECTED_TIME));

        circularSeekbar = (CircularSeekBar) getActivity().findViewById(R.id.seekBar);
        circularSeekbar.setMaxProgress(115);
        circularSeekbar.setProgress(0);
        circularSeekbar.setBarWidth(10);
        circularSeekbar.setAdjustmentFactor(150);
        circularSeekbar.initDrawable(R.drawable.stickman_sitting_1);


        circularSeekbar.setSeekBarChangeListener(new CircularSeekBar.OnSeekChangeListener() {

            @Override
            public void onProgressChange(CircularSeekBar view, int newProgress) {
                int time = round(view.getProgress(), 5) + 5;
                timeText.setText(getTimeText(time));
                Constants.TIMER_SELECTED_TIME = time;
            }
        });

        SharedPreferences prefs = getActivity().getSharedPreferences(UserPreference.MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        introShown = prefs.getBoolean("introShown", false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) {
            return;
        }

        if (introShown) {
            showStartTutorial();
        }

        circularSeekbar.showProgressMarker();
        circularSeekbar.post(new Runnable() {
            @Override
            public void run() {
                circularSeekbar.invalidate();
            }
        });

        if (Constants.IS_TIMER_SERVICE_RUNNING) {
            Intent intent = new Intent(getActivity(), TimerFragment.class);
            startActivity(intent);
        } else {
            if (!introShown) {
                MaterialIntroUtils.generateViewIdList();
                showStartTutorial();
                introShown = true;
                UserPreference.setIntroShown(getActivity(), true);
            }
        }
    }

    private void showStartTutorial() {
        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
        if (tabLayout.getSelectedTabPosition() == 0) {
            MaterialIntroView.Builder test = MaterialIntroUtils.getMainTimeText(getActivity());
            test.setInfoText(getString(R.string.tutorial_main_time_text))
                    .setTarget(timeText)
                    .setFocusType(Focus.MINIMUM)
                    .setListener(new MaterialIntroListener() {
                @Override
                public void onUserClicked(String s) {
                    MaterialIntroView.Builder test2 = MaterialIntroUtils.getMainCircleButton(getActivity());
                    test2.setInfoText(getString(R.string.tutorial_main_circle))
                            .setTarget(circularSeekbar)
                            .setListener(new MaterialIntroListener() {
                                @Override
                                public void onUserClicked(String s) {
                                    MaterialIntroView.Builder test2 = MaterialIntroUtils.getMainStartButton(getActivity());
                                    test2.setInfoText(getString(R.string.tutorial_start_button))
                                            .setTarget(button)
                                            .setFocusType(Focus.ALL)
                                            .enableDotAnimation(true)
                                            .show();
                                }
                            });
                    test2.show();
                }
            });
            test.show();
        }
    }

    private int round(double i, int v) {
        return (int) (Math.round(i / v) * v);
    }

    private String getTimeText(int progress) {
        int timeDown = (progress * 1000) * 60;
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(timeDown),
                TimeUnit.MILLISECONDS.toSeconds(timeDown) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeDown))
        );
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), TimerFragment.class);
        startActivity(intent);
    }
}