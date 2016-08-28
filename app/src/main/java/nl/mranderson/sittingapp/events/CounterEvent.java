package nl.mranderson.sittingapp.events;


public class CounterEvent {

    public final CounterAction status;

    public CounterEvent(CounterAction status) {
        this.status = status;
    }

    public enum CounterAction {

        RESTARTED,

        STOPPED,

        PENDING,

        PAUSED;

        public static CounterAction toCounterAction(String myEnumString) {
            try {
                return valueOf(myEnumString);
            } catch (Exception ex) {
                // For error cases
                return PENDING;
            }
        }

    }
}
