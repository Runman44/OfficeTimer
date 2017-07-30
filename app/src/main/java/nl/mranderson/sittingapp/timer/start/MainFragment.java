package nl.mranderson.sittingapp.timer.start;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import at.markushi.ui.CircleButton;
import nl.mranderson.sittingapp.R;
import nl.mranderson.sittingapp.common.Utils;
import nl.mranderson.sittingapp.custom.AssistantView;

public class MainFragment extends android.support.v4.app.Fragment implements MainContract.View {

    private TextView timeText;
    private Button button;
    private MainContract.Presenter presenter;
    private CircleButton plusButton;
    private CircleButton minusButton;
    private AssistantView assistant;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        minusButton = (CircleButton) view.findViewById(R.id.minus);
        plusButton = (CircleButton) view.findViewById(R.id.plus);
        timeText = (TextView) view.findViewById(R.id.timeText);
        button = (Button) view.findViewById(R.id.bStart);
        assistant = (AssistantView) view.findViewById(R.id.assistant);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPresenter();
        setListeners();
        getActivity().setTitle(R.string.title_timer);
        setAssistant();
    }

    private void setAssistant() {
        assistant.animateText("Hi ! I'm Mariska");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detach();
    }

    private void setListeners() {
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onTimeMinus();
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onTimePlus();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startTimer();
                Utils.logFirebaseEvent("START_TIMER", "BUTTON");
            }
        });
    }

    private void setPresenter() {
        presenter = createPresenter();
        presenter.attach(this);
    }

    private MainContract.Presenter createPresenter() {
        MainNavigation mainNavigation = new MainFragmentNavigation(this.getActivity());
        return new MainPresenter(mainNavigation);
    }

    @Override
    public void setTimeDisplayText(String text) {
        timeText.setText(text);
    }
}