package com.rcdvl.marvel.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.paginate.Paginate
import com.rcdvl.marvel.R
import com.rcdvl.marvel.characterlist.CharacterLoadingListItemCreator
import com.rcdvl.marvel.networking.MarvelCharacter
import com.rcdvl.marvel.networking.MarvelResponse
import com.rcdvl.marvel.networking.MarvelServiceManager
import kotlinx.android.synthetic.main.fragment_character_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by renan on 3/21/16.
 */
class CharacterSearchFragment : Fragment() {

    var query: String = ""
    var adapter: CharactersSearchAdapter? = null
    var currentCall: Call<MarvelResponse<MarvelCharacter>>? = null

    companion object {
        fun newInstance(query: String): CharacterSearchFragment {
            val fragment = CharacterSearchFragment()
            var args = Bundle()
            args.putSerializable(CharacterSearchFragment.EXTRA_QUERY, query)
            fragment.arguments = args
            return fragment
        }

        const val EXTRA_QUERY = "extra-query"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_character_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val layoutManager = LinearLayoutManager(context)
        charactersList.layoutManager = layoutManager
        charactersList.setHasFixedSize(true)

        refreshWithNewQuery()

        super.onViewCreated(view, savedInstanceState)
    }

    fun refreshWithNewQuery() {
        query = arguments.getString(EXTRA_QUERY)

        currentCall?.cancel()

        adapter = CharactersSearchAdapter()
        charactersList.adapter = adapter
        charactersList.adapter.notifyDataSetChanged()

        setupPagination()
    }

    fun setupPagination() {
        var total: Int = Int.MAX_VALUE

        val callbacks = object : Paginate.Callbacks {
            var isFirstRequest = true
            var loading: Boolean = false
            var offset = 0
            var count = 20

            override fun onLoadMore() {
                loading = true

                currentCall = MarvelServiceManager.marvelService.getCharactersList(offset, count, query)
                currentCall?.enqueue(object : Callback<MarvelResponse<MarvelCharacter>> {
                    override fun onResponse(call: Call<MarvelResponse<MarvelCharacter>>?, response: Response<MarvelResponse<MarvelCharacter>>?) {
                        if (isFirstRequest) {
                            total = response?.body()?.data?.total ?: 0
                        }
                        isFirstRequest = false
                        offset += response?.body()?.data?.count ?: count

                        Log.d("url", call?.request()?.url().toString())
                        Log.d("teste", response?.body()?.data?.results?.joinToString() ?: "")


                        adapter?.addChars(response?.body()?.data?.results ?: ArrayList())
                        adapter?.notifyItemRangeInserted(offset + 1, count)

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
                return offset >= total
            }

        }

        Paginate.with(charactersList, callbacks)
                .setLoadingTriggerThreshold(2)
                .setLoadingListItemCreator(CharacterLoadingListItemCreator())
                .build();
    }
}