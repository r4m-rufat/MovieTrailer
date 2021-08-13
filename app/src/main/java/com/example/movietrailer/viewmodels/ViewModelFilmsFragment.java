package com.example.movietrailer.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movietrailer.models.discover_model.ResultsItem;
import com.example.movietrailer.repository.films_page.DiscoverFilmsRepository;

import java.util.List;

public class ViewModelFilmsFragment extends ViewModel {

    private MutableLiveData<List<ResultsItem>> films_list = new MutableLiveData<>();

    public LiveData<List<ResultsItem>> getFilmList(){

        DiscoverFilmsRepository discoverFilmsRepository = new DiscoverFilmsRepository();
        return discoverFilmsRepository.getFilmList(
                films_list,
                "en",
                "popularity.desc",
                1,
                "flatrate"
        );
    }

}
