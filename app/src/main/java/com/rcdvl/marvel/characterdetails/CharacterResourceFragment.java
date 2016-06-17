package com.rcdvl.marvel.characterdetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;
import com.paginate.Paginate;
import com.paginate.Paginate.Callbacks;
import com.rcdvl.marvel.R;
import com.rcdvl.marvel.characterdetails.CharacterResourcePresenter.ResourceType;
import com.rcdvl.marvel.model.api.MarvelCharacter;
import com.rcdvl.marvel.model.api.MarvelResource;
import com.rcdvl.marvel.util.HorizontalSpaceItemDecoration;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by renan on 20/06/16.
 */
public class CharacterResourceFragment extends MvpLceFragment<RecyclerView, List<MarvelResource>, CharacterResourceView, CharacterResourcePresenter> implements CharacterResourceView {

    public static String EXTRA_RESOURCE_TYPE = "res-type";
    private MarvelCharacter character;
    private ResourceType resourceType;
    private Unbinder unbinder;
    private CharacterResourceAdapter adapter;
    @BindView(R.id.contentView) RecyclerView resourceList;
    @BindView(R.id.resourceTitle) TextView resourceTitle;
    @BindView(R.id.emptyView) TextView emptyView;
    @BindString(R.string.generic_error) String errorMessage;

    public static CharacterResourceFragment newInstance(MarvelCharacter character, ResourceType resourceType) {
        Bundle args = new Bundle();
        args.putParcelable(CharacterDetailsActivity.EXTRA_CHARACTER, Parcels.wrap(character));
        args.putSerializable(EXTRA_RESOURCE_TYPE, resourceType);

        CharacterResourceFragment fragment = new CharacterResourceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        character = Parcels.unwrap(getArguments().getParcelable(CharacterDetailsActivity.EXTRA_CHARACTER));
        resourceType = (ResourceType)getArguments().getSerializable(EXTRA_RESOURCE_TYPE);
        return inflater.inflate(R.layout.fragment_resources_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        adapter = new CharacterResourceAdapter();
        resourceList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        resourceList.setAdapter(adapter);
        resourceList.addItemDecoration(new HorizontalSpaceItemDecoration(8));
        resourceTitle.setText(getString(resourceType.getTitleResId()));
        emptyView.setText(getString(resourceType.getEmptyResId()));
        errorView.setVisibility(View.GONE);

        Paginate.with(resourceList, new Callbacks() {
            @Override
            public void onLoadMore() {
                loadData(false);
            }

            @Override
            public boolean isLoading() {
                return presenter.isLoading();
            }

            @Override
            public boolean hasLoadedAllItems() {
                return presenter.hasLoadedAllItems();
            }
        }).setLoadingTriggerThreshold(2).setLoadingListItemCreator(new ResourceLoadingListItemCreator()).build();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return errorMessage;
    }

    @NonNull
    @Override
    public CharacterResourcePresenter createPresenter() {
        return new CharacterResourcePresenter(character.id, resourceType);
    }

    @Override
    public void setData(List<MarvelResource> data) {
        adapter.addResources(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadResource(pullToRefresh);
    }

    @Override
    protected void onErrorViewClicked() {
        animateLoadingViewIn();
        super.onErrorViewClicked();
    }

    @Override
    public void onEmptyResourcesList() {
        if (isAdded()) {
            Log.d("onEmptyResourcesList()", "no " + resourceType.name().toLowerCase() + " found");
            errorView.setVisibility(View.GONE);
            contentView.setVisibility(View.GONE);
            loadingView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }
}
