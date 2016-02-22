package nl.mranderson.sittingapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sembozdemir.viewpagerarrowindicator.library.ViewPagerArrowIndicator;

import nl.mranderson.sittingapp.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class TutorialFragment extends Fragment {

    public TutorialFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutorial, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);

        ViewPagerArrowIndicator viewPagerArrowIndicator = (ViewPagerArrowIndicator) getActivity().findViewById(R.id.viewPagerArrowIndicator);
        viewPager.setAdapter(new TutorialPagerAdapter(getContext()));

        viewPagerArrowIndicator.bind(viewPager);

    }


}
