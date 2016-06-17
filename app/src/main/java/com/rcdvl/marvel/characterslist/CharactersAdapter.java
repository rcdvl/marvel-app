package com.rcdvl.marvel.characterslist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rcdvl.marvel.R;
import com.rcdvl.marvel.characterdetails.CharacterDetailsActivity;
import com.rcdvl.marvel.characterslist.CharactersAdapter.CharacterViewHolder;
import com.rcdvl.marvel.model.api.MarvelCharacter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by renan on 13/06/16.
 */
public class CharactersAdapter extends RecyclerView.Adapter<CharacterViewHolder> {

    private List<MarvelCharacter> characterList = new ArrayList<>();

    @Override
    public CharacterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_character, parent, false);
        return new CharacterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CharacterViewHolder holder, int position) {
        MarvelCharacter character = characterList.get(position);
        Glide.with(holder.itemView.getContext()).load(character.thumbnail.getImageUrl()).centerCrop().crossFade().into(holder.characterImage);
        holder.characterName.setText(character.name);
    }

    @Override
    public int getItemCount() {
        return characterList.size();
    }

    public void addCharacters(List<MarvelCharacter> data) {
        characterList.addAll(data);
    }

    public void addCharacters(List<MarvelCharacter> data, boolean clear) {
        if (clear) {
            characterList.clear();
        }
        characterList.addAll(data);
    }

    public void clear() {
        characterList.clear();
    }

    public class CharacterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.characterImage) ImageView characterImage;
        @BindView(R.id.characterName) TextView characterName;

        public CharacterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.characterContainer)
        public void openCharacterDetails() {
            MarvelCharacter character = characterList.get(getAdapterPosition());
            Context context = itemView.getContext();

            if (context != null) {
                Intent intent = new Intent(context, CharacterDetailsActivity.class);
                intent.putExtra(CharacterDetailsActivity.EXTRA_CHARACTER, Parcels.wrap(character));
                context.startActivity(intent);
            }
        }
    }
}
