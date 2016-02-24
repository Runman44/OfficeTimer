package nl.mranderson.sittingapp.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import nl.mranderson.sittingapp.Constants;
import nl.mranderson.sittingapp.R;
import nl.mranderson.sittingapp.custom.CircularSeekBar;

public class MainFragment extends Fragment implements View.OnClickListener {

    private CircularSeekBar circularSeekbar;
    private TextView timeText;
    private TextView motivationText;

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
        Button button = (Button) getActivity().findViewById(R.id.bStart);
        button.setOnClickListener(this);

        motivationText = (TextView) getActivity().findViewById(R.id.motivationText);
        motivationText.setText(R.string.motivation_message_1);

        ImageButton bSettings = (ImageButton) getActivity().findViewById(R.id.bSetting);
        bSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new SettingsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment, newFragment);
                transaction.commit();
            }
        });

        ImageButton bUpgrade = (ImageButton) getActivity().findViewById(R.id.bUpgrade);
        bUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), R.string.upgrade_not_there_temp, Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton bInfo = (ImageButton) getActivity().findViewById(R.id.bInfo);
        bInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new InfoFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment, newFragment);
                transaction.commit();
            }
        });
        timeText = (TextView) getActivity().findViewById(R.id.timeText);
        timeText.setText(getTimeText(Constants.TIMER_SELECTED_TIME));

        circularSeekbar = (CircularSeekBar) getActivity().findViewById(R.id.seekBar);
        circularSeekbar.setMaxProgress(115);
        circularSeekbar.setProgress(Constants.TIMER_SELECTED_TIME);
        circularSeekbar.setBarWidth(35);
        circularSeekbar.initDrawable(R.drawable.stickman_walk);


        circularSeekbar.setSeekBarChangeListener(new CircularSeekBar.OnSeekChangeListener() {

            @Override
            public void onProgressChange(CircularSeekBar view, int newProgress) {
                int time = round(view.getProgress(), 5) + 5;
                timeText.setText(getTimeText(time));
                setRangeColor(time);
                setMotivationText(time);
                Constants.TIMER_SELECTED_TIME = time;
            }
        });

        setRangeColor(Constants.TIMER_SELECTED_TIME);
        setMotivationText(Constants.TIMER_SELECTED_TIME);
    }

    @Override
    public void onResume() {
        super.onResume();
        circularSeekbar.post(new Runnable() {
            @Override
            public void run() {
                circularSeekbar.invalidate();
            }
        });
    }

    private int round(double i, int v) {
        return (int) (Math.round(i / v) * v);
    }

    private void setMotivationText(int progress) {
        if (progress <= circularSeekbar.getMaxProgress() * 0.30) {
            motivationText.setText(R.string.motivation_message_1);
        } else if (progress <= circularSeekbar.getMaxProgress() * 0.50) {
            motivationText.setText(R.string.motivation_message_2);
        }

        if (progress > circularSeekbar.getMaxProgress() * 0.60) {
            motivationText.setText(R.string.motivation_message_3);
        }
    }

    private void setRangeColor(int progress) {
        if (progress > circularSeekbar.getMaxProgress() / 2) {
            circularSeekbar.setProgressColor(ContextCompat.getColor(getActivity(), R.color.red));
        } else {
            circularSeekbar.setProgressColor(ContextCompat.getColor(getActivity(), R.color.blue));
        }
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
        Fragment newFragment = new TimerFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, newFragment);
        transaction.commit();
    }
}
