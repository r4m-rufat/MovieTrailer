package com.example.movietrailer.repository.films_page;

import static com.example.movietrailer.utils.constants.ConstantsKt.API_KEY;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.movietrailer.models.discover_model.DiscoverResponse;
import com.example.movietrailer.models.discover_model.ResultsItem;
import com.example.movietrailer.network.ApiClient;
import com.example.movietrailer.network.IApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscoverFilmsRepository {

    private static final String TAG = "DiscoverFilmsRepository";

    public MutableLiveData<List<ResultsItem>> getFilmList(
            MutableLiveData<List<ResultsItem>> film_list,
            String language,
            String sort_by, int page,
            String with_watch_monetization_types,
            MutableLiveData<Boolean> loading) {

        // loading starts in here and that's why set true
        loading.setValue(true);
        ArrayList<ResultsItem> new_film_list = new ArrayList<>();

        IApi iApi = ApiClient.getInstance().getRetrofit().create(IApi.class);

        iApi.getDiscoverInformations(
                API_KEY,
                language,
                sort_by,
                page,
                with_watch_monetization_types
        ).enqueue(new Callback<DiscoverResponse>() {
            @Override
            public void onResponse(Call<DiscoverResponse> call, Response<DiscoverResponse> response) {
                if (response.isSuccessful()) {

                    if (film_list.getValue() != null){
                        new_film_list.addAll(film_list.getValue());
                        new_film_list.addAll(response.body().getResults());
                        film_list.setValue(new_film_list);


                    }else{
                        film_list.setValue(response.body().getResults());

                    }

                    // loading finish in this block
                    loading.setValue(false);

                }

                Log.d(TAG, "onResponse: film list successfully comes");

            }

            @Override
            public void onFailure(Call<DiscoverResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: Response is failed. Reason -> " + t.getMessage());
                loading.setValue(true);
            }
        });

        return film_list;

    }



}
