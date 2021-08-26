package com.example.movietrailer.viewmodels.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movietrailer.models.authentication.User
import com.example.movietrailer.repository.account_page.AccountPageRepository

class AccountFragmentViewModel: ViewModel() {

    private val userData: MutableLiveData<User> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData(true)

    fun getUserInfo(): LiveData<User>{

        return AccountPageRepository.instance()!!.getAccountInfo(
            userData,
            loading
        )

    }

}