package com.example.movietrailer.viewmodels.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movietrailer.db.History
import com.example.movietrailer.db.HistoryDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class HistoryFragmentViewModel(app: Application): AndroidViewModel(app) {

    private var historyList: MutableLiveData<List<History>> = MutableLiveData()
    private val dao = HistoryDatabase.getHistoryDatabase((getApplication())).getDao()

    fun getHistoryList(): LiveData<List<History>>{

        postHistoryToObservableData()
        return historyList

    }

    private fun postHistoryToObservableData(){

        CoroutineScope(IO).launch {

            historyList.postValue(dao?.getAllHistoryList())

        }
    }

}