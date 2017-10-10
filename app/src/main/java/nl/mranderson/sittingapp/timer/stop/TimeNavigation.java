package nl.mranderson.sittingapp.timer.stop;

public interface TimeNavigation {

    void startTimerService(int time);

    void stopTimerService();

    void closeScreen();
}
