package nl.mranderson.sittingapp.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import nl.mranderson.sittingapp.R;

public class InfoFragment extends android.support.v4.app.Fragment implements InfoContract.View {

    private InfoContract.Presenter presenter;
    private RelativeLayout tutorialContainer;
    private RelativeLayout ratingContainer;
    private RelativeLayout contactContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        tutorialContainer = (RelativeLayout) view.findViewById(R.id.tutorial_wrapper);
        ratingContainer = (RelativeLayout) view.findViewById(R.id.rating_wrapper);
        contactContainer = (RelativeLayout) view.findViewById(R.id.contact_developer_wrapper);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = createPresenter();
        presenter.attach(this);
        setListeners();
    }

    private void setListeners() {
        tutorialContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onTutorialTapped();
            }
        });

        ratingContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onRatingTapped();
            }
        });

        contactContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onContactTapped();
            }
        });
    }

    private InfoContract.Presenter createPresenter() {
        return new InfoPresenter(new InfoFragmentNavigationImpl(getActivity()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detach();
    }
}
