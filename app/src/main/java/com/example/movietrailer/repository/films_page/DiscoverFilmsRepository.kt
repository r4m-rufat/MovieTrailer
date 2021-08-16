package com.example.movietrailer.repository.films_page

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.movietrailer.models.discover_model.ResultsItem
import com.example.movietrailer.network.IApi
import com.example.movietrailer.network.ApiClient
import com.example.movietrailer.models.discover_model.DiscoverResponse
import com.example.movietrailer.repository.films_page.DiscoverFilmsRepository
import com.example.movietrailer.utils.constants.API_KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class DiscoverFilmsRepository {

    companion object {
        private const val TAG = "DiscoverFilmsRepository"
    }

    fun getFilmList(
        film_list: MutableLiveData<List<ResultsItem>?>,
        language: String?,
        sort_by: String?, page: Int,
        with_watch_monetization_types: String?,
        loading: MutableLiveData<Boolean?>
    ): MutableLiveData<List<ResultsItem>?> {

        // loading starts in here and that's why set true
        loading.value = true
        val new_film_list = ArrayList<ResultsItem>()
        val iApi = ApiClient.getInstance().retrofit.create(IApi::class.java)
        CoroutineScope(IO).launch {

            iApi.getDiscoverInformations(
                API_KEY,
                language,
                sort_by,
                page,
                with_watch_monetization_types
            )!!.enqueue(object : Callback<DiscoverResponse?> {
                override fun onResponse(
                    call: Call<DiscoverResponse?>,
                    response: Response<DiscoverResponse?>
                ) {
                    if (response.isSuccessful) {
                        if (film_list.value != null) {
                            new_film_list.addAll(film_list.value!!)
                            new_film_list.addAll(response.body()!!.results)
                            film_list.setValue(new_film_list)
                        } else {
                            film_list.setValue(response.body()!!.results)
                        }

                        // loading finish in this block
                        loading.value = false
                    }
                    Log.d(TAG, "onResponse: film list successfully comes")
                }

                override fun onFailure(call: Call<DiscoverResponse?>, t: Throwable) {
                    Log.d(TAG, "onFailure: Response is failed. Reason -> " + t.message)
                    loading.value = true
                }
            })

        }
        return film_list
    }
}