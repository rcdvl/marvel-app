package com.rcdvl.marvel.characterslist;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;
import com.paginate.Paginate;
import com.paginate.Paginate.Callbacks;
import com.rcdvl.marvel.R;
import com.rcdvl.marvel.model.api.MarvelCharacter;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CharactersListFragment extends MvpLceFragment<CoordinatorLayout, List<MarvelCharacter>, CharactersView, CharactersPresenter> implements CharactersView {

    protected Unbinder unbinder;
    protected CharactersAdapter adapter;
    private Snackbar snackbar;
    @BindView(R.id.charactersList) protected RecyclerView charactersList;
    @BindView(R.id.listContainer) View listContainer;
    @BindString(R.string.network_loading_error) String loadingError;

    @NonNull
    @Override
    public CharactersPresenter createPresenter() {
        return new CharactersPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_character_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        if (adapter == null) {
            adapter = new CharactersAdapter();
        }
        charactersList.setLayoutManager(new LinearLayoutManager(getActivity()));
        charactersList.setAdapter(adapter);

        Paginate.with(charactersList, new Callbacks() {
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
        }).setLoadingTriggerThreshold(2).setLoadingListItemCreator(new CharacterLoadingListItemCreator()).build();
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return loadingError;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        dismissSnackbar();
    }

    @Override
    public void setData(List<MarvelCharacter> data) {
        adapter.addCharacters(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        if (presenter.isFirstLoad()) {
            animateLoadingViewIn();
        }

        hideErrors();
        presenter.loadMoreCharacters(pullToRefresh);
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        String errorMsg = getErrorMessage(e, pullToRefresh);

        if (presenter.isFirstLoad()) {
            errorView.setText(errorMsg);
            animateErrorViewIn();
        } else {
            showLightError(errorMsg);
        }

//        super.showError(e, pullToRefresh);
    }

    @Override
    protected void showLightError(String msg) {
        snackbar = Snackbar.make(contentView, msg, Snackbar.LENGTH_INDEFINITE).setAction(R.string.retry, new OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData(false);
            }
        });
        snackbar.show();
    }

    protected void hideErrors() {
        dismissSnackbar();
        errorView.setVisibility(View.GONE);
    }

    private void dismissSnackbar() {
        if (snackbar != null && snackbar.isShownOrQueued()) {
            snackbar.dismiss();
        }
    }
}
