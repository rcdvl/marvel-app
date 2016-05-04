package com.rcdvl.marvel.characterdetails

import android.util.Log
import com.paginate.Paginate
import com.rcdvl.marvel.networking.MarvelResource
import com.rcdvl.marvel.networking.MarvelResponse
import com.rcdvl.marvel.networking.MarvelServiceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by Renan on 22/03/2016.
 */
class Callback : Paginate.Callbacks {
    var loading: Boolean = false
    var offset = 0
    val count = 20
    var total = 0
    var isFirstRequest = true
    val resourceType: String
    val characterId: Long
    val adapter: CharacterResourceAdapter

    constructor(resourceType: String, characterId: Long, adapter: CharacterResourceAdapter) {
        this.resourceType = resourceType
        this.characterId = characterId
        this.adapter = adapter
    }

    override fun onLoadMore() {
        loading = true

        MarvelServiceManager.marvelService.getCharacterResourceList(resourceType, characterId, offset, count)
                .enqueue(object : Callback<MarvelResponse<MarvelResource>> {
                    override fun onResponse(call: Call<MarvelResponse<MarvelResource>>?, response: Response<MarvelResponse<MarvelResource>>?) {
                        if (resourceType == "stories") {
                            Log.d("teste", response?.body()?.data?.results?.joinToString() ?: "")
                        }

                        if (isFirstRequest) {
                            total = response?.body()?.data?.total ?: 0
                        }

                        isFirstRequest = false
                        offset += count

                        adapter.addResources(response?.body()?.data?.results ?: ArrayList())
                        adapter.notifyItemRangeInserted(offset + 1, count)

                        loading = false
                    }

                    override fun onFailure(call: Call<MarvelResponse<MarvelResource>>?, t: Throwable?) {
                        Log.d("teste", "falhou: " + t?.message)
                        loading = false
                    }

                })
    }

    override fun isLoading(): Boolean {
        return loading
    }

    override fun hasLoadedAllItems(): Boolean {
        if (isFirstRequest) {
            return false
        } else {
            return offset >= total
        }
    }
}