package nl.mranderson.sittingapp.timer.start;


import nl.mranderson.sittingapp.common.Utils;

public class MainPresenter implements MainContract.Presenter {

    private static final int DEFAULT_TIME = 5;
    private final MainNavigation navigation;

    private MainContract.View view;
    private int time = DEFAULT_TIME;

    public MainPresenter(MainNavigation navigation) {
        this.navigation = navigation;
    }

    @Override
    public void attach(MainContract.View view) {
        this.view = view;
        this.view.setTimeDisplayText(convertProgressToTime(time));
    }

    @Override
    public void detach() {
        view = null;
    }

    @Override
    public void startTimer() {
        navigation.startTimer(time);
    }

    @Override
    public void onTimePlus() {
        time += 5;
        displayTime(time);
    }

    @Override
    public void onTimeMinus() {
        if (time != 5) {
            time -= 5;
            displayTime(time);
        }
    }

    private void displayTime(int time) {
        String displayText = convertProgressToTime(time);
        view.setTimeDisplayText(displayText);
    }

    private String convertProgressToTime(int progress) {
        //TODO support hours.
        return Utils.formatDate((progress * 1000) * 60);
    }

}
