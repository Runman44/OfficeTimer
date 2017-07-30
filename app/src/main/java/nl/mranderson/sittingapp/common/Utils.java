package nl.mranderson.sittingapp.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.concurrent.TimeUnit;

//import static com.google.firebase.analytics.FirebaseAnalytics.Event.SELECT_CONTENT;

//TODO no static pls
public abstract class Utils {

    @Deprecated
    public static boolean isPlayServiceAvailable(Context context) {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS;
    }

    public static Dialog getErrorDialogPlayService(Activity context) {
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (code != ConnectionResult.SUCCESS) {
            Dialog errorDialog = GoogleApiAvailability.getInstance().getErrorDialog(context, code, 9);
            return errorDialog;
        }
        return null;
    }

    public static String formatDate(long timeDown) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(timeDown),
                TimeUnit.MILLISECONDS.toSeconds(timeDown) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeDown)));
    }

    public static int formatMillisToMin(long millis) {
        return (int) TimeUnit.MILLISECONDS.toMinutes(millis);
    }

    public static void logFirebaseEvent(String event, String contentType) {
        // [START custom_event]
//        Bundle params = new Bundle();
//        params.putString(FirebaseAnalytics.Param.ITEM_ID, event);
//        params.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType);
//        Constants.INSTANCE.getFIREBASE_ANALYTICS().logEvent(SELECT_CONTENT, params);
        // [END custom_event]
    }

    public static int round(double i, int v) {
        return (int) (Math.round(i / v) * v);
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Activity activity) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
