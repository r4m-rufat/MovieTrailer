package com.example.movietrailer.repository.cast_page

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.movietrailer.models.person.cast_detail.CastDetailResponse
import com.example.movietrailer.models.person.cast_images.CastImagesResponse
import com.example.movietrailer.network.ApiClient
import com.example.movietrailer.network.IApi
import com.example.movietrailer.utils.constants.API_KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class CastDetailRepository {

    companion object{
        private const val TAG = "CastDetailRepository"
        private var INSTANCE: CastDetailRepository? = null
        private val client = ApiClient.getInstance().retrofit.create(IApi::class.java)

        fun instance(): CastDetailRepository{
            if (INSTANCE == null){
                INSTANCE = CastDetailRepository()
            }
            return INSTANCE!!
        }

    }

    fun getCastDetailInformations(
        id: Int,
        language: String,
        loading: MutableLiveData<Boolean>,
    ): MutableLiveData<CastDetailResponse>{

        val detailResult = MutableLiveData<CastDetailResponse>()

        CoroutineScope(IO).launch {

            val response = client.getCastDetailInfo(
                id,
                API_KEY,
                language
            ).awaitResponse()

            if (response.isSuccessful){
                detailResult.postValue(response.body())
                loading.postValue(false)
                Log.d(TAG, "getCastDetailInformations: Detail response successfully comes")
            }else{
                loading.postValue(false)
                Log.d(TAG, "getCastDetailInformations: Response code is ${response.code()}")
            }

        }

        return detailResult

    }

    fun getCastImagesResult(
        id: Int,
    ): MutableLiveData<CastImagesResponse>{

        val imagesResult = MutableLiveData<CastImagesResponse>()

        CoroutineScope(IO).launch {

            val response = client.getCastImages(
                id,
                API_KEY
            ).awaitResponse()

            if (response.isSuccessful){
                imagesResult.postValue(response.body())
                Log.d(TAG, "getCastImagesResult: Images successfully comes")
            }else{
                Log.d(TAG, "getCastImagesResult: Response code is ${response.code()}")
            }

        }
        return imagesResult
    }

}