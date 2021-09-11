package com.example.movietrailer.repository.films_page

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.movietrailer.models.discover_model.ResultsItem
import com.example.movietrailer.network.ApiClient
import com.example.movietrailer.network.IApi
import com.example.movietrailer.utils.constants.API_KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class SearchFilmsRepository {

    companion object {

        private var INSTANCE: SearchFilmsRepository? = null
        private const val TAG = "SearchFilmsRepository"
        fun instance(): SearchFilmsRepository {

            if (INSTANCE == null) {
                INSTANCE = SearchFilmsRepository()
            }
            return INSTANCE as SearchFilmsRepository
        }

    }

    fun getSearchFilmList(
        film_list: MutableLiveData<List<ResultsItem>?>,
        language: String?,
        query: String,
        loading: MutableLiveData<Boolean?>,
        page: Int
    ): MutableLiveData<List<ResultsItem>?> {

        // loading starts in here and that's why set true
        loading.value = true
        var new_search_result: ArrayList<ResultsItem>? = ArrayList()
        var old_search_result: List<ResultsItem>?
        val iApi = ApiClient.getInstance().retrofit.create(IApi::class.java)
        CoroutineScope(IO).launch {

            val response = iApi.getSearchInformations(
                API_KEY,
                language,
                query,
                page
            ).awaitResponse()

            CoroutineScope(IO).launch {
                if (response.isSuccessful) {
                    delay(1000L)
                    loading.postValue(false)
                    old_search_result = film_list.value
                    old_search_result?.let {
                        new_search_result!!.addAll(it)
                    }
                    new_search_result!!.addAll(response.body()!!.results)
                    film_list.postValue(new_search_result)
                } else {
                    Log.d(TAG, "getSearchFilmList: Search result is failed ${response.code()}")
                    loading.postValue(true)
                }
            }

        }
        return film_list
    }

    fun getSuggestionSearchList(
        film_list: MutableLiveData<List<ResultsItem>?>,
        query: String,
        loading: MutableLiveData<Boolean?>,
        page: Int
    ): MutableLiveData<List<ResultsItem>?> {

        // loading starts in here and that's why set true
        loading.value = true
        val iApi = ApiClient.getInstance().retrofit.create(IApi::class.java)
        CoroutineScope(Dispatchers.IO).launch {

            val response = iApi.getSuggestionSearch(
                API_KEY,
                query,
                page
            ).awaitResponse()

            if (response.isSuccessful) {
                film_list.postValue(response.body()?.results)
                loading.postValue(false)
            } else {
                Log.d(TAG, "getSearchFilmList: Search result is failed ${response.code()}")
                loading.postValue(false)
            }

        }
        return film_list
    }

}