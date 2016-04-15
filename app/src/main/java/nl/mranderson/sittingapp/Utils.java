package nl.mranderson.sittingapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public abstract class Utils {

    //Check for Google play services available on device
    //TODO dep !
    public static boolean isPlayServiceAvailable(Context context) {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS;
    }

    public static Dialog getErrorDialogPlayService(Activity context){
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (code != ConnectionResult.SUCCESS){
            Dialog errorDialog = GoogleApiAvailability.getInstance().getErrorDialog(context, code, 9);
            return errorDialog;
        }
        return null;
    }

}
