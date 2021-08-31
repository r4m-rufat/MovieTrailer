package com.example.movietrailer.viewmodels.films

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movietrailer.models.discover_model.ResultsItem
import com.example.movietrailer.repository.films_page.CategoryFilmsRepository
import com.example.movietrailer.repository.films_page.DiscoverFilmsRepository
import com.example.movietrailer.repository.films_page.SearchFilmsRepository
import com.example.movietrailer.utils.default_lists.TopCategoriesItem
import kotlinx.coroutines.launch

class FilmsFragmentViewModel : ViewModel() {

    private val TAG = "ViewModelFilmsFragment"
    private val films_list = MutableLiveData<List<ResultsItem>?>()
    var page = MutableLiveData(1)
    var loading = MutableLiveData(true)
    var query = MutableLiveData<String>()
    var categorySelectedItem = MutableLiveData<TopCategoriesItem>(null)
    var sort_by = MutableLiveData("popularity.desc")
    var genreIds = MutableLiveData("")
    var voteAverage = MutableLiveData(5)

    var filterGenreList: MutableLiveData<List<Int>> = MutableLiveData(mutableListOf())

    val filmList: LiveData<List<ResultsItem>?>
        get() {
            getDataSetToMutableLiveData()
            return films_list
        }


    private fun getDataSetToMutableLiveData() {

        viewModelScope.launch {

            val discoverFilmsRepository = DiscoverFilmsRepository()
            discoverFilmsRepository.getFilmList(
                films_list,
                "en",
                sort_by.value,
                genreIds.value,
                voteAverage.value,
                page.value!!,
                "flatrate",
                loading
            )

            Log.d(TAG, "getDataSetToMutableLiveData: " + sort_by.value)

        }

    }

    val searchResult: LiveData<List<ResultsItem>?>
        get() {
            getSearchDataSetToMutableLiveData()
            return films_list
        }

    private fun getSearchDataSetToMutableLiveData() {

        viewModelScope.launch {

            SearchFilmsRepository.instance().getSearchFilmList(
                films_list,
                "en",
                query.value!!,
                loading,
                page.value!!
            )

        }

    }

    fun getCategoryFilmListWhenClicked(clickedItem: TopCategoriesItem): LiveData<List<ResultsItem>?>{

        viewModelScope.launch {

            when(clickedItem){

                TopCategoriesItem.DISCOVER -> {

                    DiscoverFilmsRepository().getFilmList(
                        films_list,
                        "en",
                        sort_by.value,
                        genreIds.value,
                        voteAverage.value,
                        page.value!!,
                        "flatrate",
                        loading
                    )

                }

                TopCategoriesItem.TOP_RATED -> {
                    CategoryFilmsRepository.instance()!!.getTopRatedFilmList(
                        films_list,
                        "en",
                        loading,
                        page.value!!
                    )
                }

                TopCategoriesItem.POPULAR -> {

                    CategoryFilmsRepository.instance()!!.getPopularFilmList(
                        films_list,
                        "en",
                        loading,
                        page.value!!
                    )

                }

                TopCategoriesItem.UP_COMING -> {

                    CategoryFilmsRepository.instance()!!.getUpComingFilmList(
                        films_list,
                        "en",
                        loading,
                        page.value!!
                    )

                }

                TopCategoriesItem.NOW_PLAYING -> {

                    CategoryFilmsRepository.instance()!!.getNowPlayingFilmList(
                        films_list,
                        "en",
                        loading,
                        page.value!!
                    )

                }

            }

        }

        return films_list

    }

    fun incrementPageNumber() {
        page.value = page.value!! + 1
    }

    fun resetVariables(){
        films_list.value = null
        page.value = 1
    }

}