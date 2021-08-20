package com.example.movietrailer.network

import com.example.movietrailer.models.detail_model.details.DetailResponse
import com.example.movietrailer.models.discover_model.DiscoverResponse
import com.example.movietrailer.models.discover_model.ResultsItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("movie/top_rated?")
    fun getTopRatedInformations(
        @Query("api_key") api_key: String?,
        @Query("language") language: String?,
        @Query("page") page: Int,
    ): Call<DiscoverResponse>

    @GET("movie/popular?")
    fun getPopularInformations(
        @Query("api_key") api_key: String?,
        @Query("language") language: String?,
        @Query("page") page: Int,
    ): Call<DiscoverResponse>

    @GET("movie/upcoming?")
    fun getUpComingInformations(
        @Query("api_key") api_key: String?,
        @Query("language") language: String?,
        @Query("page") page: Int,
    ): Call<DiscoverResponse>

    @GET("movie/now_playing?")
    fun getNowPlayingInformations(
        @Query("api_key") api_key: String?,
        @Query("language") language: String?,
        @Query("page") page: Int,
    ): Call<DiscoverResponse>

    @GET("movie/{movie_id}?")
    fun getFilmDetailInformations(
        @Path("movie_id") id: Int,
        @Query("api_key") api_key: String,
        @Query("language") language: String?,

    ): Call<DetailResponse>

}