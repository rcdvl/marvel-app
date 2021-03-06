package com.rcdvl.marvel.characterlist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.paginate.recycler.LoadingListItemCreator
import com.rcdvl.marvel.R

/**
 * Created by renan on 3/17/16.
 */
class CharacterLoadingListItemCreator : LoadingListItemCreator {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        val inflater = LayoutInflater.from(parent?.context);
        val view = inflater.inflate(R.layout.character_loading_list_item, parent, false);
        return VH(view);
    }
}

class VH(view: View) : RecyclerView.ViewHolder(view) {

}
