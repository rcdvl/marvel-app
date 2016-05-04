package com.rcdvl.marvel.search

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.rcdvl.marvel.R
import com.rcdvl.marvel.characterdetails.CharacterDetailsActivity
import com.rcdvl.marvel.characterlist.CharactersAdapter
import com.rcdvl.marvel.networking.MarvelCharacter
import java.util.*

/**
 * Created by renan on 3/17/16.
 */
class CharactersSearchAdapter : RecyclerView.Adapter<CharacterViewHolder>() {

    val characters: ArrayList<MarvelCharacter>
    var recyclerView: RecyclerView? = null

    init {
        characters = ArrayList()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CharacterViewHolder? {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_character_search, parent, false)

        v.setOnClickListener {
            val pos = recyclerView?.getChildAdapterPosition(it) ?: 0
            val intent: Intent = Intent(recyclerView?.context, CharacterDetailsActivity::class.java)
            intent.putExtra(CharactersAdapter.EXTRA_CHARACTER, characters[pos])
            (recyclerView?.context as Activity).startActivity(intent)
        }

        return CharacterViewHolder(v)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder?, position: Int) {
        holder?.characterName?.text = characters[position].name

        Glide.with(holder?.itemView?.context)
                .load(characters[position].thumbnail.path + '.' + characters[position].thumbnail.extension)
                .centerCrop()
                .crossFade()
                .into(holder?.characterImage)
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    fun addChars(marvelCharacters: ArrayList<MarvelCharacter>) {
        characters.addAll(marvelCharacters)
    }
}

class CharacterViewHolder : RecyclerView.ViewHolder {

    val characterName: TextView
    val characterImage: ImageView

    constructor(view: View) : super(view) {
        characterName = view.findViewById(R.id.characterName) as TextView
        characterImage = view.findViewById(R.id.characterImage) as ImageView
    }

}
