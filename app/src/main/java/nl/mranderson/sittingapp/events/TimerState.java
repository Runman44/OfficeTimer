package nl.mranderson.sittingapp.events;


public enum TimerState {

    START,

    PENDING,

    RUNNING,

    STOPPED,

    MOVING;

    public static TimerState toApplicationState(String myEnumString) {
        try {
            return valueOf(myEnumString);
        } catch (Exception ex) {
            // For error cases
            return PENDING;
        }
    }
}
