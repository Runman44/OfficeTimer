package nl.mranderson.sittingapp;

/**
 * Created by MrAnderson1 on 20/01/16.
 * <p/>
 * Constants used in the whole app.
 */
public class Constants {

    public static final int NOTIFICATION_GET_WALKING = 99;
    public static final String SENSOR_BROADCAST = "sensor";
    public static final String COUNTDOWN_STOP_TIMER_BROADCAST = "countdown_freeze";
    public static long TIMER_SERVICE_MILLI_LEFT = 0;

    public static boolean IS_TIMER_SERVICE_RUNNING = false;
    public static int TIMER_SELECTED_TIME = 5;

    public static final String COUNTDOWN_TIME_BROADCAST = "time";
    public static final String COUNTDOWN_RESTART_BROADCAST = "restart";
    public static final String COUNTDOWN_STOP_BROADCAST = "stop";

}
