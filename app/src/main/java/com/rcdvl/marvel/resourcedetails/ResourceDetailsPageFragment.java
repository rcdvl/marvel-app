package com.rcdvl.marvel.resourcedetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rcdvl.marvel.R;
import com.rcdvl.marvel.model.api.MarvelResource;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by renan on 04/07/16.
 */
public class ResourceDetailsPageFragment extends Fragment {

    public static String EXTRA_RESOURCE = "extra-resource";
    private Unbinder unbinder;
    @BindView(R.id.resourceText) TextView resourceText;
    @BindView(R.id.resourceImage) ImageView resourceImage;

    public static Fragment newInstance(MarvelResource resource) {
        ResourceDetailsPageFragment fragment = new ResourceDetailsPageFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_RESOURCE, Parcels.wrap(resource));
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_resource_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);
        MarvelResource resource = Parcels.unwrap(getArguments().getParcelable(EXTRA_RESOURCE));
        Glide.with(getContext()).load(resource.thumbnail.path + "." + resource.thumbnail.extension).crossFade().into(resourceImage);
        resourceText.setText(resource.title);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
