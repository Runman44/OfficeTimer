package nl.mranderson.sittingapp.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.mranderson.sittingapp.Constants;
import nl.mranderson.sittingapp.MaterialIntroUtils;
import nl.mranderson.sittingapp.R;
import nl.mranderson.sittingapp.Utils;

public class InfoFragment extends android.support.v4.app.Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.tutorial_wrapper, R.id.contact_developer_wrapper, R.id.rating_wrapper})
    public void onWrapperClicked(View v) {
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

        Utils.logFirebaseEvent("TUTORIAL", "BUTTON");
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

        Utils.logFirebaseEvent("RATE", "BUTTON");
    }

    private void mailIntent() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@mranderson.nl"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Sitting Up!");
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }

        Utils.logFirebaseEvent("MAIL", "BUTTON");
    }
}
