package com.example.movietrailer.repository.details_page

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.movietrailer.models.detail_model.details.DetailResponse
import com.example.movietrailer.models.detail_model.similar_films.SimilarResponse
import com.example.movietrailer.network.ApiClient
import com.example.movietrailer.network.IApi
import com.example.movietrailer.utils.constants.API_KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class SimilarFilmsRepository {

    companion object {

        private const val TAG = "SimilarFilmsRepository"
        private var INSTANCE: SimilarFilmsRepository? = null

        fun instance(): SimilarFilmsRepository? {
            if (INSTANCE == null) {
                INSTANCE = SimilarFilmsRepository()
            }
            return INSTANCE
        }

    }

    fun getSimilarFilmData(
        similar_film_list: MutableLiveData<SimilarResponse>,
        loading: MutableLiveData<Boolean>,
        language: String,
        id: Int
    ): MutableLiveData<SimilarResponse> {

        CoroutineScope(Dispatchers.IO).launch {

            val api = ApiClient.getInstance().retrofit.create(IApi::class.java)
            val response =
                api.getSimilarFilmsInformation(id = id, language = language, api_key = API_KEY)
                    .awaitResponse();

            if (response.isSuccessful) {
                loading.postValue(false)
                similar_film_list.postValue(response.body())
                Log.d(
                    TAG, "getSimilarFilmData: info successfully comes -> ${response.code()}"
                )
            } else {
                loading.postValue(true)
                Log.d(
                    TAG, "getSimilarFilmData: info is failed -> ${response.code()}"
                )
            }

        }

        return similar_film_list

    }


}