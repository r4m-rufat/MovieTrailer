package com.example.movietrailer.repository.films_page

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.movietrailer.models.discover_model.DiscoverResponse
import com.example.movietrailer.models.discover_model.ResultsItem
import com.example.movietrailer.network.ApiClient
import com.example.movietrailer.network.IApi
import com.example.movietrailer.utils.constants.API_KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.awaitResponse

class CategoryFilmsRepository {

    companion object{
        private const val TAG = "CategoryFilmsRepository"
        private var INSTANCE: CategoryFilmsRepository? = null
        fun instance(): CategoryFilmsRepository?{

            if (INSTANCE == null){
                INSTANCE = CategoryFilmsRepository()
            }
            return INSTANCE
        }
    }

    fun getTopRatedFilmList(
        film_list: MutableLiveData<List<ResultsItem>?>,
        language: String?,
        loading: MutableLiveData<Boolean?>,
        page: Int
    ): MutableLiveData<List<ResultsItem>?> {

        // loading starts in here and that's why set true
        loading.value = true
        var new_film_list = ArrayList<ResultsItem>()
        var old_film_list: List<ResultsItem>?
        val iApi = ApiClient.getInstance().retrofit.create(IApi::class.java)
        CoroutineScope(Dispatchers.IO).launch {

            val response = iApi.getTopRatedInformations(
                API_KEY,
                language,
                page = page
            ).awaitResponse()

            if (response.isSuccessful) {
                if (film_list.value != null){
                    old_film_list = film_list.value
                    old_film_list?.let {
                        new_film_list!!.addAll(it)
                    }
                }
                new_film_list!!.addAll(response.body()!!.results)
                film_list.postValue(new_film_list)
                loading.postValue(false)
                Log.d(TAG, "getTopRatedFilmList: top rated films successfully comes")
            } else {
                Log.d(TAG, "getSearchFilmList: Search result is failed ${response.code()}")
                loading.postValue(true)
            }


        }
        return film_list
    }

    fun getPopularFilmList(
        film_list: MutableLiveData<List<ResultsItem>?>,
        language: String?,
        loading: MutableLiveData<Boolean?>,
        page: Int
    ): MutableLiveData<List<ResultsItem>?> {

        // loading starts in here and that's why set true
        loading.value = true
        var new_film_list: ArrayList<ResultsItem>? = ArrayList()
        var old_film_list: List<ResultsItem>?
        val iApi = ApiClient.getInstance().retrofit.create(IApi::class.java)
        CoroutineScope(Dispatchers.IO).launch {

            val response = iApi.getPopularInformations(
                API_KEY,
                language,
                page = page
            ).awaitResponse()

            if (response.isSuccessful) {
                old_film_list = film_list.value
                old_film_list?.let {
                    new_film_list!!.addAll(it)
                }
                new_film_list!!.addAll(response.body()!!.results)
                film_list.postValue(new_film_list)
                loading.postValue(false)
                Log.d(TAG, "getPopularFilmList: Popular films comes ${response.body()!!.results[0].id}")
            } else {
                Log.d(TAG, "getSearchFilmList: Category result is failed ${response.code()}")
                loading.postValue(true)
            }


        }
        return film_list
    }

    fun getUpComingFilmList(
        film_list: MutableLiveData<List<ResultsItem>?>,
        language: String?,
        loading: MutableLiveData<Boolean?>,
        page: Int
    ): MutableLiveData<List<ResultsItem>?> {

        // loading starts in here and that's why set true
        loading.value = true
        var new_film_list: ArrayList<ResultsItem>? = ArrayList()
        var old_film_list: List<ResultsItem>?
        val iApi = ApiClient.getInstance().retrofit.create(IApi::class.java)
        CoroutineScope(Dispatchers.IO).launch {

            val response = iApi.getUpComingInformations(
                API_KEY,
                language,
                page = page
            ).awaitResponse()

            if (response.isSuccessful) {
                old_film_list = film_list.value
                old_film_list?.let {
                    new_film_list!!.addAll(it)
                }
                new_film_list!!.addAll(response.body()!!.results)
                film_list.postValue(new_film_list)
                loading.postValue(false)
            } else {
                Log.d(TAG, "getSearchFilmList: Upcoming result is failed ${response.code()}")
                loading.postValue(true)
            }


        }
        return film_list
    }

    fun getNowPlayingFilmList(
        film_list: MutableLiveData<List<ResultsItem>?>,
        language: String?,
        loading: MutableLiveData<Boolean?>,
        page: Int
    ): MutableLiveData<List<ResultsItem>?> {

        // loading starts in here and that's why set true
        loading.value = true
        var new_film_list: ArrayList<ResultsItem>? = ArrayList()
        var old_film_list: List<ResultsItem>?
        val iApi = ApiClient.getInstance().retrofit.create(IApi::class.java)
        CoroutineScope(Dispatchers.IO).launch {

            val response = iApi.getNowPlayingInformations(
                API_KEY,
                language,
                page = page
            ).awaitResponse()

            if (response.isSuccessful) {
                old_film_list = film_list.value
                old_film_list?.let {
                    new_film_list!!.addAll(it)
                }
                new_film_list!!.addAll(response.body()!!.results)
                film_list.postValue(new_film_list)
                loading.postValue(false)
            } else {
                Log.d(TAG, "getSearchFilmList: Noting result is failed ${response.code()}")
                loading.postValue(true)
            }

        }
        return film_list
    }

}