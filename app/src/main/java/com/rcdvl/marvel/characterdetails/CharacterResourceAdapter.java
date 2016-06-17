package com.rcdvl.marvel.characterdetails;

import android.content.Intent;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rcdvl.marvel.R;
import com.rcdvl.marvel.characterdetails.CharacterResourceAdapter.ResourceViewHolder;
import com.rcdvl.marvel.model.api.MarvelResource;
import com.rcdvl.marvel.resourcedetails.ResourceDetailsActivity;
import com.rcdvl.marvel.resourcedetails.ResourceDetailsFragment;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by renan on 20/06/16.
 */
public class CharacterResourceAdapter extends Adapter<ResourceViewHolder> {

    private ArrayList<MarvelResource> resources;

    public CharacterResourceAdapter() {
        resources = new ArrayList<>();
    }

    @Override
    public ResourceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_character_resource, parent, false);
        return new ResourceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResourceViewHolder holder, int position) {
        MarvelResource resource = resources.get(position);
        if (resource != null) {
            holder.resourceText.setText(resource.title);

            if (resource.thumbnail != null) {
                Glide.with(holder.itemView.getContext()).load(resource.thumbnail.getImageUrl()).centerCrop().crossFade().into(holder.resourceImage);
            }
        }
    }

    @Override
    public int getItemCount() {
        return resources.size();
    }

    public void addResources(List<MarvelResource> resources) {
        this.resources.addAll(resources);
    }

    public class ResourceViewHolder extends ViewHolder {
        @BindView(R.id.resourceImage) ImageView resourceImage;
        @BindView(R.id.resourceText) TextView resourceText;

        public ResourceViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), ResourceDetailsActivity.class);
                    intent.putExtra(ResourceDetailsFragment.EXTRA_RESOURCES, Parcels.wrap(resources));
                    intent.putExtra(ResourceDetailsFragment.EXTRA_SELECTED_INDEX, getAdapterPosition());

                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
