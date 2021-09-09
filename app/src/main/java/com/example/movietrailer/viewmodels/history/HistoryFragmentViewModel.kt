package com.example.movietrailer.viewmodels.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movietrailer.db.History
import com.example.movietrailer.db.HistoryDatabase
import com.google.firebase.auth.FirebaseAuth
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

            var list: MutableList<History> = mutableListOf()

            try {
                for (film in dao!!.getAllHistoryList()){
                    if (film.uid == FirebaseAuth.getInstance().currentUser!!.uid){
                        list.add(film)
                    }
                }
            }catch (e: NullPointerException){
                e.printStackTrace()
            }
            historyList.postValue(list)

        }
    }

}