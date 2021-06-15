package ru.androidschool.intensiv.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.data.TvShow
import ru.androidschool.intensiv.network.responses.ActorsResponse
import ru.androidschool.intensiv.network.responses.MovieDetailsResponse
import ru.androidschool.intensiv.network.responses.MoviesResponse

interface MovieApiInterface {

    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = DEFAULT_LANGUAGE
    ): Call<MoviesResponse<Movie>>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = DEFAULT_LANGUAGE
    ): Call<MoviesResponse<Movie>>

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = DEFAULT_LANGUAGE
    ): Call<MoviesResponse<Movie>>

    @GET("tv/popular")
    fun getTvShows(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = DEFAULT_LANGUAGE
    ): Call<MoviesResponse<TvShow>>

    @GET("movie/{movie_id}")
    fun getMovieDetailsById(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = DEFAULT_LANGUAGE
    ): Call<MovieDetailsResponse>

    @GET("movie/{movie_id}/credits")
    fun getActorsMovieById(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = DEFAULT_LANGUAGE
    ): Call<ActorsResponse>

    companion object {
        private const val API_KEY = BuildConfig.THE_MOVIE_DATABASE_API
        private const val DEFAULT_LANGUAGE = "ru-RU"
    }
}
