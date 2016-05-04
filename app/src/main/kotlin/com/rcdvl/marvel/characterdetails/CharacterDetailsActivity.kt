package com.rcdvl.marvel.characterdetails

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.rcdvl.marvel.R
import com.rcdvl.marvel.characterlist.CharactersAdapter
import com.rcdvl.marvel.networking.MarvelCharacter
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by renan on 3/18/16.
 */
class CharacterDetailsActivity : AppCompatActivity() {

    private var _isDestroyed = false
    var isAvailable: Boolean
        get() = _isDestroyed
        set(value) {
            _isDestroyed = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isAvailable = true

        setContentView(R.layout.activity_character_details)
        setSupportActionBar(toolbar);

        toggleHomeAsUp(true)

        Log.d("teste", "character: ${intent.extras.get(CharactersAdapter.EXTRA_CHARACTER) as MarvelCharacter}")

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, CharacterDetailsFragment.newInstance(
                            intent.extras.get(CharactersAdapter.EXTRA_CHARACTER) as MarvelCharacter))
                    .commit()
        }
    }

    override fun onDestroy() {
        isAvailable = false
        super.onDestroy()
    }

    fun toggleHomeAsUp(active: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(active)
        supportActionBar?.setDisplayShowHomeEnabled(active)

        if (active) {
            toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_back)
        }
    }
}