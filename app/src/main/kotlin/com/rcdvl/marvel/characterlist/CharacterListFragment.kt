package com.rcdvl.marvel.characterlist

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import com.paginate.Paginate
import com.rcdvl.marvel.R
import com.rcdvl.marvel.networking.MarvelCharacter
import com.rcdvl.marvel.networking.MarvelResponse
import com.rcdvl.marvel.networking.MarvelServiceManager
import kotlinx.android.synthetic.main.fragment_character_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by renan on 3/17/16.
 */
class CharacterListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater?.inflate(R.layout.fragment_character_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val layoutManager = LinearLayoutManager(context)
        charactersList.layoutManager = layoutManager
        charactersList.setHasFixedSize(true)

        val adapter = CharactersAdapter()
        charactersList.adapter = adapter

        val callbacks = object : Paginate.Callbacks {
            var loading: Boolean = false
            var offset = 0
            val count = 20

            override fun onLoadMore() {
                loading = true

                MarvelServiceManager.marvelService.getCharactersList(offset, count).enqueue(object : Callback<MarvelResponse<MarvelCharacter>> {
                    override fun onResponse(call: Call<MarvelResponse<MarvelCharacter>>?, response: Response<MarvelResponse<MarvelCharacter>>?) {
                        Log.d("teste", response?.body()?.data?.results?.joinToString() ?: "")

                        adapter.addChars(response?.body()?.data?.results ?: ArrayList())
                        adapter.notifyItemRangeInserted(offset + 1, count)

                        offset += count
                        loading = false
                    }

                    override fun onFailure(call: Call<MarvelResponse<MarvelCharacter>>?, t: Throwable?) {
                        Log.d("teste", "falhou: " + t?.message)
                        loading = false
                    }

                })
            }

            override fun isLoading(): Boolean {
                return loading
            }

            override fun hasLoadedAllItems(): Boolean {
                return false
            }

        }

        Paginate.with(charactersList, callbacks)
                .setLoadingTriggerThreshold(2)
                .addLoadingListItem(true)
                .setLoadingListItemCreator(CharacterLoadingListItemCreator())
                .build();

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
    }
}
