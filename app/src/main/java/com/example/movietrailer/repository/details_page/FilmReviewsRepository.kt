package com.example.movietrailer.repository.details_page

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.movietrailer.models.film_reviews.ResultsItem
import com.example.movietrailer.network.ApiClient
import com.example.movietrailer.network.IApi
import com.example.movietrailer.utils.constants.API_KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class FilmReviewsRepository {

    companion object{
        private const val TAG = "FilmReviewsRepository"
        private var INSTANCE: FilmReviewsRepository? = null
        fun instance(): FilmReviewsRepository{
            if (INSTANCE == null){
                INSTANCE = FilmReviewsRepository()
            }
            return INSTANCE!!
        }
    }

    fun getFilmReviews(
        id: Int
    ): MutableLiveData<List<ResultsItem>>{

        var reviewsList: MutableLiveData<List<ResultsItem>> = MutableLiveData()

        val client = ApiClient.getInstance().retrofit.create(IApi::class.java)

        CoroutineScope(IO).launch {
            val response = client.getFilmReviews(
                id,
                API_KEY,
                "en-US",
                1
            ).awaitResponse()

            if (response.isSuccessful){
                reviewsList.postValue(response.body()?.results)
                Log.d(TAG, "getFilmReviews: review list successfully comes.")
            }else{
                Log.d(
                    TAG,
                    "getFilmReviews: review list is null now. Error is ${response.errorBody()}"
                )
            }
        }

        return reviewsList

    }

}