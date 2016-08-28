package nl.mranderson.sittingapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.concurrent.TimeUnit;

import static com.google.firebase.analytics.FirebaseAnalytics.Event.SELECT_CONTENT;

public abstract class Utils {

    //Check for Google play services available on device
    //TODO dep !
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

    public static void logFirebaseEvent(String event){
        // [START custom_event]
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, event);
        Constants.FIREBASE_ANALYTICS.logEvent(SELECT_CONTENT, params);
        // [END custom_event]
    }

    public static int round(double i, int v) {
        return (int) (Math.round(i / v) * v);
    }

}
