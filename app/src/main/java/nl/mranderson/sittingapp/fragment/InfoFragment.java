package nl.mranderson.sittingapp.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import nl.mranderson.sittingapp.Constants;
import nl.mranderson.sittingapp.MaterialIntroUtils;
import nl.mranderson.sittingapp.R;

import static com.google.firebase.analytics.FirebaseAnalytics.Event.SELECT_CONTENT;

public class InfoFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    public InfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RelativeLayout tutorial = (RelativeLayout) getActivity().findViewById(R.id.tutorial_wrapper);
        tutorial.setOnClickListener(this);

        RelativeLayout view = (RelativeLayout) getActivity().findViewById(R.id.contact_developer_wrapper);
        view.setOnClickListener(this);

        RelativeLayout rating = (RelativeLayout) getActivity().findViewById(R.id.rating_wrapper);
        rating.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case (R.id.tutorial_wrapper):
                resetTutorial();
                break;
            case (R.id.rating_wrapper):
                rateIntent();
                break;
            case (R.id.contact_developer_wrapper):
                mailIntent();
                break;
        }
    }

    private void resetTutorial() {
        Constants.SHOW_TUTORIAL = true;
        MaterialIntroUtils.generateViewIdList();
        Toast.makeText(getActivity(), "Tutorial Enabled", Toast.LENGTH_SHORT).show();

        // [START custom_event]
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, "TUTORIAL");
        Constants.FIREBASE_ANALYTICS.logEvent(SELECT_CONTENT, params);
        // [END custom_event]
    }

    private void rateIntent() {
        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
        }

        // [START custom_event]
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, "RATE");
        Constants.FIREBASE_ANALYTICS.logEvent(SELECT_CONTENT, params);
        // [END custom_event]
    }

    private void mailIntent() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@mranderson.nl"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Sitting Up!");
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }

        // [START custom_event]
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, "MAIL");
        Constants.FIREBASE_ANALYTICS.logEvent(SELECT_CONTENT, params);
        // [END custom_event]
    }
}
