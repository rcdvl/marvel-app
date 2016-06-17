package com.rcdvl.marvel.characterdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.rcdvl.marvel.R;
import com.rcdvl.marvel.model.api.MarvelCharacter;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by renan on 14/06/16.
 */
public class CharacterDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_CHARACTER = "extra-character";

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            MarvelCharacter character = Parcels.unwrap(extras.getParcelable(EXTRA_CHARACTER));
            getSupportFragmentManager().beginTransaction().replace(R.id.container, CharacterDetailsFragment.newInstance(character)).commit();
        }
    }
}
