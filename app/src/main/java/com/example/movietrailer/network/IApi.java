package com.example.movietrailer.network;

import com.example.movietrailer.models.discover_model.DiscoverResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IApi {

    @GET("discover/movie?")
    Call<DiscoverResponse> getDiscoverInformations(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("sort_by") String sort_by,
            @Query("page") int page,
            @Query("with_watch_monetization_types") String with_watch_monetization_types
    );

}
