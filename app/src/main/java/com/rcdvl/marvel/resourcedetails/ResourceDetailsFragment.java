package com.rcdvl.marvel.resourcedetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rcdvl.marvel.R;
import com.rcdvl.marvel.model.api.MarvelResource;
import com.rcdvl.marvel.util.AndroidUtils;
import com.rcdvl.marvel.util.ZoomOutSlideTransformer;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by renan on 04/07/16.
 */
public class ResourceDetailsFragment extends Fragment {

    public static String EXTRA_RESOURCES = "extra-resources";
    public static String EXTRA_SELECTED_INDEX = "extra-selected-index";
    @BindView(R.id.pager) ViewPager pager;
    @BindView(R.id.pageIndicator) TextView pageIndicator;
    private ArrayList<MarvelResource> resources;
    private Unbinder unbinder;

    public static ResourceDetailsFragment newInstance(ArrayList<MarvelResource> resources, int selectedIndex) {
        ResourceDetailsFragment fragment = new ResourceDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_RESOURCES, Parcels.wrap(resources));
        args.putInt(EXTRA_SELECTED_INDEX, selectedIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_resource_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);
        resources = Parcels.unwrap(getArguments().getParcelable(EXTRA_RESOURCES));

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageIndicator.setText("" + (position + 1) + "/" + resources.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pager.setAdapter(new ResourceDetailsPagerAdapter(getFragmentManager()));
        pageIndicator.setText("1/" + resources.size());
        pager.setPageMargin((int)AndroidUtils.dpToPx(8f));

        pager.setCurrentItem(getArguments().getInt(EXTRA_SELECTED_INDEX), false);
        pager.setPageTransformer(true, new ZoomOutSlideTransformer());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_resource_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.close) {
            getActivity().onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    class ResourceDetailsPagerAdapter extends FragmentStatePagerAdapter {

        public ResourceDetailsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ResourceDetailsPageFragment.newInstance(resources.get(position));
        }

        @Override
        public int getCount() {
            return resources.size();
        }
    }
}
