package com.example.movietrailer.viewmodels.persons

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movietrailer.models.person.cast_detail.CastDetailResponse
import com.example.movietrailer.models.person.cast_images.CastImagesResponse
import com.example.movietrailer.repository.cast_page.CastDetailRepository

class CastDetailFragmentViewModel: ViewModel() {

    private var detailResult: MutableLiveData<CastDetailResponse> = MutableLiveData<CastDetailResponse>()
    private var castImages: MutableLiveData<CastImagesResponse> = MutableLiveData<CastImagesResponse>()
    val loading: MutableLiveData<Boolean> = MutableLiveData(true)

    fun getCastDetail(id: Int): LiveData<CastDetailResponse>{
        detailResult = CastDetailRepository.instance().getCastDetailInformations(
            id,
            "en",
            loading = loading
        )
        return detailResult
    }

    fun getImages(id: Int): LiveData<CastImagesResponse>{
        castImages = CastDetailRepository.instance().getCastImagesResult(
            id
        )
        return castImages
    }

}