package com.rcdvl.marvel.characterdetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.paginate.Paginate
import com.rcdvl.marvel.R
import com.rcdvl.marvel.characterlist.CharacterLoadingListItemCreator
import com.rcdvl.marvel.characterlist.CharactersAdapter
import com.rcdvl.marvel.networking.MarvelCharacter
import com.rcdvl.marvel.networking.MarvelListWrapper
import com.rcdvl.marvel.util.HorizontalSpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_character_details.*
import java.util.*

/**
 * Created by renan on 3/18/16.
 */
class CharacterDetailsFragment : Fragment() {

    var character: MarvelCharacter? = null

    companion object {
        fun newInstance(character: MarvelCharacter): CharacterDetailsFragment {
            val fragment: CharacterDetailsFragment = CharacterDetailsFragment()
            var args = Bundle()
            args.putSerializable(CharactersAdapter.EXTRA_CHARACTER, character)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        character = arguments.getSerializable(CharactersAdapter.EXTRA_CHARACTER) as MarvelCharacter?
        var view = inflater?.inflate(R.layout.fragment_character_details, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Glide.with(context)
                .load(character?.thumbnail?.path + '.' + character?.thumbnail?.extension)
                .centerCrop()
                .crossFade()
                .into(characterImage);

        Glide.with(context)
                .load(R.drawable.background)
                .into(backgroundImage)

        characterName.text = character?.name

        if (TextUtils.isEmpty(character?.description)) {
            descriptionContainer.visibility = View.GONE
        } else {
            descriptionText.text = character?.description
        }

        comicsList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        seriesList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        storiesList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        eventsList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        if (character != null) {
            setupPagination(comicsList, "comics")
            setupPagination(seriesList, "series")
            setupPagination(storiesList, "stories")
            setupPagination(eventsList, "events")
        }

        comicsList.addItemDecoration(HorizontalSpaceItemDecoration())
        seriesList.addItemDecoration(HorizontalSpaceItemDecoration())
        storiesList.addItemDecoration(HorizontalSpaceItemDecoration())
        eventsList.addItemDecoration(HorizontalSpaceItemDecoration())

        hideIfEmpty(comicsContainer, character?.comics)
        hideIfEmpty(seriesContainer, character?.series)
        hideIfEmpty(storiesContainer, character?.stories)
        hideIfEmpty(eventsContainer, character?.events)

        setMiscButtonListener(detailButton, "detail")
        setMiscButtonListener(wikiButton, "wiki")
        setMiscButtonListener(comiclinkButton, "comiclink")

        contentLayout.requestFocus()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun hideIfEmpty(view: View, list: MarvelListWrapper?) {
        if (list?.items == null || list?.items?.size == 0) view.visibility = View.GONE
    }

    private fun setupPagination(list: RecyclerView, resourceType: String) {
        var adapter: CharacterResourceAdapter? = CharacterResourceAdapter(ArrayList())
        list.adapter = adapter

        Paginate.with(list, Callback(resourceType, character!!.id, adapter!!))
                .setLoadingListItemCreator(CharacterLoadingListItemCreator())
                .build();
    }

    private fun setMiscButtonListener(button: View, type: String) {
        button.setOnClickListener({
            val intent = Intent(Intent.ACTION_VIEW)
            val detailUrl = character?.urls?.first { it.type.equals("$type", true) }

            if (detailUrl != null) {
                intent.data = Uri.parse(detailUrl.url)
                activity.startActivity(intent)
            }
        })

    }
}

