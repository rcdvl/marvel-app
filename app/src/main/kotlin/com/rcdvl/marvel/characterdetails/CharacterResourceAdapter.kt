package com.rcdvl.marvel.characterdetails

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.rcdvl.marvel.R
import com.rcdvl.marvel.networking.MarvelResource
import com.rcdvl.marvel.resourcedetails.ResourceDetailsActivity
import com.rcdvl.marvel.resourcedetails.ResourceDetailsFragment
import java.util.*

/**
 * Created by renan on 3/17/16.
 */
class CharacterResourceAdapter : RecyclerView.Adapter<ResourceViewHolder> {

    val resources: ArrayList<MarvelResource>
    var recyclerView: RecyclerView? = null

    constructor(resources: ArrayList<MarvelResource>) {
        this.resources = resources
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ResourceViewHolder? {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_character_resource, parent, false)
        return ResourceViewHolder(v)
    }

    override fun onBindViewHolder(holder: ResourceViewHolder?, position: Int) {
        holder?.resourceText?.text = resources[position].title

        holder?.itemView?.setOnClickListener {
            val intent = Intent(recyclerView?.context, ResourceDetailsActivity::class.java)
            intent.putExtra(ResourceDetailsFragment.EXTRA_RESOURCES, resources)
            intent.putExtra(ResourceDetailsFragment.EXTRA_SELECTED_INDEX, position)

            (recyclerView?.context as AppCompatActivity).startActivity(intent)
        }

        val resource = resources[position]
        if (resource.thumbnail != null) {
            loadImage(holder, "${resource.thumbnail?.path}.${resource.thumbnail?.extension}")
        }

        //        Log.d("teste", MarvelServiceManager.buildUri(resource.items[position].resourceURI))
    }

    private fun loadImage(holder: ResourceViewHolder?, url: String) {
        if ((holder?.itemView?.context as CharacterDetailsActivity).isAvailable) {
            Glide.with(holder?.itemView?.context as Activity)
                    .load(url)
                    .centerCrop()
                    .crossFade()
                    .into(holder?.resourceImage)
        }
    }

    fun addResources(resources: ArrayList<MarvelResource>) {
        this.resources.addAll(resources)
    }

    override fun getItemCount(): Int {
        return resources.size
    }
}

class ResourceViewHolder : RecyclerView.ViewHolder {

    val resourceText: TextView
    val resourceImage: ImageView

    constructor(view: View) : super(view) {
        resourceText = view.findViewById(R.id.resourceText) as TextView
        resourceImage = view.findViewById(R.id.resourceImage) as ImageView
    }

}
