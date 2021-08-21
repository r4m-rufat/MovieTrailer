package com.example.movietrailer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movietrailer.models.detail_model.casts.CastResponse
import com.example.movietrailer.models.detail_model.details.DetailResponse
import com.example.movietrailer.models.detail_model.similar_films.SimilarResponse
import com.example.movietrailer.models.detail_model.video.VideoResponse
import com.example.movietrailer.repository.details_page.FilmDetailRepository
import com.example.movietrailer.repository.details_page.SimilarFilmsRepository

class FilmDetailFragmentViewModel: ViewModel() {

    private val filmDetail: MutableLiveData<DetailResponse> = MutableLiveData()
    private val castList: MutableLiveData<CastResponse> = MutableLiveData()
    private val similarList: MutableLiveData<SimilarResponse> = MutableLiveData()
    private val videoResponse: MutableLiveData<VideoResponse> = MutableLiveData()
    private var loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    fun getFilmList(id: Int): LiveData<DetailResponse>{

        return FilmDetailRepository.instance()!!.getFilmDetailData(
            filmDetail,
            loading,
            "en",
            id
        )

    }

    fun getCastsList(id: Int): LiveData<CastResponse>{

        return FilmDetailRepository.instance()!!.getCastsData(
            castList,
            loading,
            "en",
            id
        )

    }

    fun getSimilarFilmList(id: Int): LiveData<SimilarResponse>{

        return SimilarFilmsRepository.instance()!!.getSimilarFilmData(
            similarList,
            language = "en",
            loading = loading,
            id = id
        )

    }

    fun getVideo(videoID: Int): LiveData<VideoResponse>{

        return FilmDetailRepository.instance()!!.getVideoData(
            videoResponse,
            "en",
            videoID
        )

    }

}