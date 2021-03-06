package com.rcdvl.marvel.characterdetails;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paginate.recycler.LoadingListItemCreator;
import com.rcdvl.marvel.R;

/**
 * Created by renan on 14/06/16.
 */
public class ResourceLoadingListItemCreator implements LoadingListItemCreator {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.resource_loading_list_item, parent, false);
        return new LoadingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    class LoadingViewHolder extends ViewHolder {

        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
