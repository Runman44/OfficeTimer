package nl.mranderson.sittingapp.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.sembozdemir.viewpagerarrowindicator.library.ViewPagerArrowIndicator;

import nl.mranderson.sittingapp.R;

public class InfoFragment extends Fragment implements View.OnClickListener {

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

        ImageButton bBack = (ImageButton) getActivity().findViewById(R.id.bBack);
        bBack.setOnClickListener(this);

        ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);

        ViewPagerArrowIndicator viewPagerArrowIndicator = (ViewPagerArrowIndicator) getActivity().findViewById(R.id.viewPagerArrowIndicator);
        viewPager.setAdapter(new InfoPagerAdapter(getActivity()));

        viewPagerArrowIndicator.bind(viewPager);

    }

    @Override
    public void onClick(View v) {
        Fragment newFragment = new MainFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, newFragment);
        transaction.commit();
    }

}
