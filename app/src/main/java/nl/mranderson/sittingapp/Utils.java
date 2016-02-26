package nl.mranderson.sittingapp;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public abstract class Utils {

    //Check for Google play services available on device
    public static boolean isPlayServiceAvailable(Context context) {
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS;
    }
}
