package nl.mranderson.sittingapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.mobiwise.materialintro.animation.MaterialIntroListener;
import co.mobiwise.materialintro.shape.Focus;
import nl.mranderson.sittingapp.Constants;
import nl.mranderson.sittingapp.MaterialIntroUtils;
import nl.mranderson.sittingapp.R;
import nl.mranderson.sittingapp.UserPreference;
import nl.mranderson.sittingapp.Utils;
import nl.mranderson.sittingapp.activity.TimerActivity;
import nl.mranderson.sittingapp.custom.CircularSeekBar;
import nl.mranderson.sittingapp.events.TimerState;

public class MainFragment extends android.support.v4.app.Fragment {

    private static final int DEFAULT_TIME = 5;
    private boolean isFirstStart;

    @BindView(R.id.seekBar)
    CircularSeekBar circularSeekbar;
    @BindView(R.id.timeText)
    TextView timeText;
    @BindView(R.id.bStart)
    Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isFirstStart = UserPreference.getIntroShown(getActivity());

        timeText.setText(getTimeText(DEFAULT_TIME));

        circularSeekbar.setMaxProgress(115);
        circularSeekbar.setProgress(0);
        circularSeekbar.setBarWidth(10);
        circularSeekbar.setAdjustmentFactor(150);
        circularSeekbar.initDrawable(R.drawable.stickman_sitting_1);

        circularSeekbar.setSeekBarChangeListener(new CircularSeekBar.OnSeekChangeListener() {

            @Override
            public void onProgressChange(CircularSeekBar view, int newProgress) {
                int time = Utils.round(view.getProgress(), 5) + 5;
                timeText.setText(getTimeText(time));
                UserPreference.setCounterTime(getActivity(), time);
            }
        });
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

        if (isFirstStart) {
            MaterialIntroUtils.generateViewIdList();
            showStartTutorial();
            isFirstStart = false;
        }

        if (Constants.SHOW_TUTORIAL) {
            showStartTutorial();
        }

        circularSeekbar.showProgressMarker();
        circularSeekbar.post(new Runnable() {
            @Override
            public void run() {
                circularSeekbar.invalidate();
            }
        });

        // this needs to be in onResume because when you open the app again when timer is running it will start in the onResume.
        if (TimerState.toApplicationState(UserPreference.getTimerStatus(getActivity())) == TimerState.RUNNING || TimerState.toApplicationState(UserPreference.getTimerStatus(getActivity())) == TimerState.MOVING) {
            Intent intent = new Intent(getActivity(), TimerActivity.class);
            startActivity(intent);
        } else {
            UserPreference.setCounterTime(getActivity(), DEFAULT_TIME);
            timeText.setText(getTimeText(DEFAULT_TIME));
            circularSeekbar.setProgress(0);
        }
    }

    //TODO this is very ugly...
    private void showStartTutorial() {
        final boolean[] opened1 = {true, true};
        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
        if (tabLayout.getSelectedTabPosition() == 0) {

            MaterialIntroUtils.getMainTimeText(getActivity(), timeText, getString(R.string.tutorial_main_time_text))
                    .setFocusType(Focus.MINIMUM)
                    .setListener(new MaterialIntroListener() {
                        @Override
                        public void onUserClicked(String s) {
                            if (opened1[0]) {
                                opened1[0] = false;
                                MaterialIntroUtils.getMainCircleButton(getActivity(), circularSeekbar, getString(R.string.tutorial_main_circle))
                                        .setListener(new MaterialIntroListener() {
                                            @Override
                                            public void onUserClicked(String s) {
                                                if (opened1[1]) {
                                                    opened1[1] = false;
                                                    MaterialIntroUtils.getMainStartButton(getActivity(), button, getString(R.string.tutorial_start_button))
                                                            .setFocusType(Focus.ALL)
                                                            .enableDotAnimation(true)
                                                            .show();
                                                }
                                            }
                                        }).show();
                            }
                        }
                    }).show();


        }
    }

    private String getTimeText(int progress) {
        return Utils.formatDate((progress * 1000) * 60);
    }

    @OnClick(R.id.bStart)
    public void onStartClicked() {
        UserPreference.setTimerStatus(getActivity(), TimerState.START.toString());
        startActivity(new Intent(getActivity(), TimerActivity.class));

        Utils.logFirebaseEvent("START_TIMER");
    }
}