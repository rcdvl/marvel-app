package com.rcdvl.marvel.characterdetails;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rcdvl.marvel.R;
import com.rcdvl.marvel.characterdetails.CharacterResourcePresenter.ResourceType;
import com.rcdvl.marvel.model.api.MarvelCharacter;
import com.rcdvl.marvel.model.api.MarvelUrl;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by renan on 16/06/16.
 */
public class CharacterDetailsFragment extends Fragment {

    private MarvelCharacter character;
    private Unbinder unbinder;
    @BindView(R.id.characterImage) ImageView characterImage;
    @BindView(R.id.characterName) TextView characterName;
    @BindView(R.id.characterDescription) TextView characterDescription;

    public static CharacterDetailsFragment newInstance(MarvelCharacter character) {
        Bundle args = new Bundle();
        args.putParcelable(CharacterDetailsActivity.EXTRA_CHARACTER, Parcels.wrap(character));

        CharacterDetailsFragment fragment = new CharacterDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        character = Parcels.unwrap(getArguments().getParcelable(CharacterDetailsActivity.EXTRA_CHARACTER));
        return inflater.inflate(R.layout.fragment_character_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        Glide.with(this).load(character.thumbnail.getImageUrl()).centerCrop().into(characterImage);

        characterName.setText(character.name);

        if (TextUtils.isEmpty(character.description)) {
            view.findViewById(R.id.descriptionContainer).setVisibility(View.GONE);
        } else {
            characterDescription.setText(character.description);
        }


        if (savedInstanceState == null) {
            CharacterResourceFragment fragment = CharacterResourceFragment.newInstance(character, ResourceType.COMICS);
            getChildFragmentManager().beginTransaction().replace(R.id.comicsListContainer, fragment).commit();

            fragment = CharacterResourceFragment.newInstance(character, ResourceType.SERIES);
            getChildFragmentManager().beginTransaction().replace(R.id.seriesListContainer, fragment).commit();

            fragment = CharacterResourceFragment.newInstance(character, ResourceType.STORIES);
            getChildFragmentManager().beginTransaction().replace(R.id.storiesListContainer, fragment).commit();

            fragment = CharacterResourceFragment.newInstance(character, ResourceType.EVENTS);
            getChildFragmentManager().beginTransaction().replace(R.id.eventsListContainer, fragment).commit();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.detailButton, R.id.comiclinkButton, R.id.wikiButton})
    public void launchButtonLink(View button) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        MarvelUrl detailUrl = null;
        String type = (String)button.getTag();

        for (MarvelUrl marvelUrl : character.urls) {
            if (type.equals(marvelUrl.type)) {
                detailUrl = marvelUrl;
                break;
            }
        }

        if (detailUrl != null) {
            intent.setData(Uri.parse(detailUrl.url));
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), getString(R.string.link_not_found), Toast.LENGTH_LONG).show();
        }
    }
}
