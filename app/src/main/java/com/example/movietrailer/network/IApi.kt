package com.example.movietrailer.network

import com.example.movietrailer.models.discover_model.DiscoverResponse
import com.example.movietrailer.models.discover_model.ResultsItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IApi {

    @GET("discover/movie?")
    fun getDiscoverInformations(
        @Query("api_key") api_key: String?,
        @Query("language") language: String?,
        @Query("sort_by") sort_by: String?,
        @Query("page") page: Int,
        @Query("with_watch_monetization_types") with_watch_monetization_types: String?
    ): Call<DiscoverResponse?>?

    @GET("search/movie?")
    fun getSearchInformations(
        @Query("api_key") api_key: String?,
        @Query("language") language: String?,
        @Query("query") query: String,
        @Query("page") page: Int,
    ): Call<DiscoverResponse>

}