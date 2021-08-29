package com.example.movietrailer.viewmodels.wish_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movietrailer.models.wish_list.WishList
import com.example.movietrailer.repository.wish_list.WishLIstRepository

class WishListFragmentViewModel: ViewModel() {

    var loading: MutableLiveData<Boolean> = MutableLiveData(true)

    fun getWishList(): LiveData<List<WishList>>{

        return WishLIstRepository.getInstance().getWishList(loading)

    }

}