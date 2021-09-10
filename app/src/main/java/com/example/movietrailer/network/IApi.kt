package com.example.movietrailer.network

import com.example.movietrailer.models.detail_model.casts.CastResponse
import com.example.movietrailer.models.detail_model.details.DetailResponse
import com.example.movietrailer.models.detail_model.similar_films.SimilarResponse
import com.example.movietrailer.models.detail_model.video.VideoResponse
import com.example.movietrailer.models.discover_model.DiscoverResponse
import com.example.movietrailer.models.film_reviews.ReviewResponse
import com.example.movietrailer.models.person.cast_detail.CastDetailResponse
import com.example.movietrailer.models.person.cast_images.CastImagesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IApi {

    @GET("discover/movie?")
    fun getDiscoverInformations(
        @Query("api_key") api_key: String?,
        @Query("language") language: String?,
        @Query("sort_by") sort_by: String?,
        @Query("with_genres") genreIds: String?,
        @Query("vote_average.gte") vote_average: Int?,
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

    @GET("movie/{movie_id}/credits?")
    fun getFilmCastsInformation(
        @Path("movie_id") id: Int,
        @Query("api_key") api_key: String,
        @Query("language") language: String?,

        ): Call<CastResponse>

    @GET("movie/{movie_id}/similar?")
    fun getSimilarFilmsInformation(
        @Path("movie_id") id: Int,
        @Query("api_key") api_key: String,
        @Query("language") language: String?,

        ): Call<SimilarResponse>

    @GET("movie/{movie_id}/videos?")
    fun getVideoTrailer(
        @Path("movie_id") id: Int,
        @Query("api_key") api_key: String,
        @Query("language") language: String?,

        ): Call<VideoResponse>

    @GET("person/{person_id}?")
    fun getCastDetailInfo(
        @Path("person_id") id: Int,
        @Query("api_key") api_key: String,
        @Query("language") language: String?,
        ): Call<CastDetailResponse>

    @GET("person/{person_id}/images?")
    fun getCastImages(
        @Path("person_id") id: Int,
        @Query("api_key") api_key: String,
    ): Call<CastImagesResponse>

    @GET("movie/{movie_id}/reviews")
    fun getFilmReviews(
        @Path("movie_id") id: Int,
        @Query("api_key") api_key: String,
        @Query("language") language: String?,
        @Query("page") page: Int,
    ): Call<ReviewResponse>

    @GET("search/movie?")
    fun getSuggestionSearch(
        @Query("api_key") api_key: String?,
        @Query("query") query: String,
        @Query("page") page: Int,
    ): Call<DiscoverResponse>

}