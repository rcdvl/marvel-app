package com.rcdvl.marvel.resourcedetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.rcdvl.marvel.R;
import com.rcdvl.marvel.model.api.MarvelResource;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by renan on 04/07/16.
 */
public class ResourceDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            ArrayList<MarvelResource> res = Parcels.unwrap(intent.getParcelableExtra(ResourceDetailsFragment.EXTRA_RESOURCES));
            int index = intent.getIntExtra(ResourceDetailsFragment.EXTRA_SELECTED_INDEX, 0);

            getSupportFragmentManager().beginTransaction().replace(R.id.container, ResourceDetailsFragment.newInstance(res, index)).commit();
        }
    }
}
