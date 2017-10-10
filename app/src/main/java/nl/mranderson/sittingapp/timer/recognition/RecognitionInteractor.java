package nl.mranderson.sittingapp.timer.recognition;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;

import nl.mranderson.sittingapp.common.UserPreference;
import nl.mranderson.sittingapp.common.Utils;

public class RecognitionInteractor {

    private GoogleApiClient mGApiClient;
    private Context context;
    private UserPreference userPreference;

    public RecognitionInteractor(Context context, UserPreference userPreference) {
        this.context = context;
        this.userPreference = userPreference;
    }

    public void startSensors(final Listener listener) {
        //TODO check this outside this class?
        if (Utils.isPlayServiceAvailable(context)) {
            mGApiClient = new GoogleApiClient.Builder(context)
                    .addApi(ActivityRecognition.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(@Nullable Bundle bundle) {
                            onRecognitionConnected(mGApiClient);
                            listener.onRecognitionConnection();
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            listener.onRecognitionConnectionSuspended();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            listener.onRecognitionConnectionFailed();
                        }
                    })
                    .build();
            mGApiClient.connect();
        } else {
            //TODO ?
        }
    }

    public void stopSensors() {
        if (mGApiClient != null) {
            disconnectRecognitionService(mGApiClient);
            disconnectGoogleClient(mGApiClient);
        }
    }

    private void onRecognitionConnected(GoogleApiClient mGApiClient) {
        Intent intent = new Intent(context, ActivityRecognitionIntentService.class);
        intent.putExtra("sensitivity", userPreference.getSensitivity());
        PendingIntent mActivityRecognitionPendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGApiClient, 5000, mActivityRecognitionPendingIntent);
    }

    private void disconnectGoogleClient(GoogleApiClient mGApiClient) {
        mGApiClient.disconnect();
    }

    private void disconnectRecognitionService(GoogleApiClient mGApiClient) {
        //todo crash if not connected yet
        Intent i = new Intent(context, ActivityRecognitionIntentService.class);
        PendingIntent mActivityRecognitionPendingIntent = PendingIntent
                .getService(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(mGApiClient, mActivityRecognitionPendingIntent);
    }

    public interface Listener {

        void onRecognitionConnection();

        void onRecognitionConnectionSuspended();

        void onRecognitionConnectionFailed();
    }
}
