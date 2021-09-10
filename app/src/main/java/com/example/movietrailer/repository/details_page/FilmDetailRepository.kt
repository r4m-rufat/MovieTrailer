package com.example.movietrailer.repository.details_page

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.movietrailer.models.detail_model.casts.CastResponse
import com.example.movietrailer.models.detail_model.details.DetailResponse
import com.example.movietrailer.models.detail_model.video.VideoResponse
import com.example.movietrailer.models.wish_list.WishList
import com.example.movietrailer.network.ApiClient
import com.example.movietrailer.network.IApi
import com.example.movietrailer.utils.constants.API_KEY
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class FilmDetailRepository {

    companion object {

        private const val TAG = "FilmDetailRepository"
        private var INSTANCE: FilmDetailRepository? = null

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        private val db = FirebaseDatabase.getInstance()
        val reference = db.reference

        fun instance(): FilmDetailRepository? {
            if (INSTANCE == null) {
                INSTANCE = FilmDetailRepository()
            }
            return INSTANCE
        }

    }

    fun getFilmDetailData(
        detail_list: MutableLiveData<DetailResponse>,
        loading: MutableLiveData<Boolean>,
        language: String,
        id: Int
    ): MutableLiveData<DetailResponse>{

        CoroutineScope(IO).launch {

            val api = ApiClient.getInstance().retrofit.create(IApi::class.java)
            val response =
                api.getFilmDetailInformations(id = id, language = language, api_key = API_KEY)
                    .awaitResponse();

            if (response.isSuccessful) {
                loading.postValue(false)
                detail_list.postValue(response.body())
                Log.d(
                    TAG,
                    "getFilmDetailData: detail info successfully comes -> ${response.code()}"
                )
            } else {
                loading.postValue(true)
                Log.d(TAG, "getFilmDetailData: detail is failed -> ${response.code()}")
            }

        }

        return detail_list

    }

    fun getCastsData(
        cast_list: MutableLiveData<CastResponse>,
        loading: MutableLiveData<Boolean>,
        language: String,
        id: Int
    ): MutableLiveData<CastResponse> {

        CoroutineScope(IO).launch {

            val api = ApiClient.getInstance().retrofit.create(IApi::class.java)
            val response =
                api.getFilmCastsInformation(id = id, language = language, api_key = API_KEY)
                    .awaitResponse();

            if (response.isSuccessful) {
                loading.postValue(false)
                cast_list.postValue(response.body())
                Log.d(
                    TAG,
                    "getFilmDetailData: detail info successfully comes -> ${response.code()}"
                )
            } else {
                loading.postValue(true)
                Log.d(TAG, "getFilmDetailData: detail is failed -> ${response.code()}")
            }

        }

        return cast_list

    }

    fun getVideoData(
        video_response: MutableLiveData<VideoResponse>,
        language: String,
        id: Int
    ): MutableLiveData<VideoResponse> {

        CoroutineScope(IO).launch {

            val api = ApiClient.getInstance().retrofit.create(IApi::class.java)
            val response =
                api.getVideoTrailer(id = id, language = language, api_key = API_KEY)
                    .awaitResponse();

            if (response.isSuccessful) {
                video_response.postValue(response.body())
                Log.d(
                    TAG,
                    "getVideoData: video info successfully comes -> ${response.code()}"
                )
            } else {
                Log.d(TAG, "getVideoData: video is failed -> ${response.code()}")
            }

        }

        return video_response

    }

    fun addFilmToGlobalDatabase(filmID: Int, filmImage: String, filmTitle: String, filmGenres: String, voteAverage: Double){

        val wishList = WishList(filmID, filmImage, filmTitle, filmGenres, voteAverage)

        reference.child("user_wish_list")
            .child(firebaseUser!!.uid)
            .push()
            .setValue(wishList)

    }

    fun checkFilmInDatabase(filmID: Int, boolean: MutableLiveData<Boolean>): MutableLiveData<Boolean>{

        CoroutineScope(IO).launch {

            reference.child("user_wish_list")
                .child(firebaseUser!!.uid)
                .addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (snapshot.exists()){

                            for (single_snapshot in snapshot.children){

                                if (filmID == single_snapshot.getValue(WishList::class.java)?.filmID){
                                    boolean.postValue(true)
                                    break
                                }

                            }
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        boolean.postValue(true)
                        Log.d(TAG, "onCancelled: ${error.message}")
                    }

                })

        }

        return boolean

    }

    fun removeFilmFromGlobalDatabase(filmID: Int){


        CoroutineScope(IO).launch {

            reference.child("user_wish_list")
                .child(firebaseUser!!.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){

                            for (single_snapshot in snapshot.children){

                                if (filmID == single_snapshot.getValue(WishList::class.java)?.filmID){

                                    single_snapshot.ref.removeValue()

                                }

                            }

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d(TAG, "onCancelled: ${error.message}")
                    }

                })

        }

    }

}