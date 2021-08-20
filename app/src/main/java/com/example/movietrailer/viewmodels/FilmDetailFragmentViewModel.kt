package com.example.movietrailer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movietrailer.models.detail_model.details.DetailResponse
import com.example.movietrailer.repository.details_page.FilmDetailRepository

class FilmDetailFragmentViewModel: ViewModel() {

    private val filmDetail: MutableLiveData<DetailResponse> = MutableLiveData()
    private var loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    fun getFilmList(id: Int): LiveData<DetailResponse>{

        return FilmDetailRepository.instance()!!.getFilmDetailData(
            filmDetail,
            loading,
            "en",
            id
        )

    }

}