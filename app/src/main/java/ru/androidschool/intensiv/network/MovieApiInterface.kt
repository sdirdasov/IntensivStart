package ru.androidschool.intensiv.network

import io.reactivex.rxjava3.core.Observable
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
    ): Observable<MoviesResponse<Movie>>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = DEFAULT_LANGUAGE
    ): Observable<MoviesResponse<Movie>>

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = DEFAULT_LANGUAGE
    ): Observable<MoviesResponse<Movie>>

    @GET("tv/popular")
    fun getTvShows(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = DEFAULT_LANGUAGE
    ): Observable<MoviesResponse<TvShow>>

    @GET("movie/{movie_id}")
    fun getMovieDetailsById(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = DEFAULT_LANGUAGE
    ): Observable<MovieDetailsResponse>

    @GET("movie/{movie_id}/credits")
    fun getActorsMovieById(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = DEFAULT_LANGUAGE
    ): Observable<ActorsResponse>

    @GET("search/movie")
    fun searchMovie(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = DEFAULT_LANGUAGE,
        @Query("query") query: String
    ): Observable<MoviesResponse<Movie>>

    companion object {
        private const val API_KEY = BuildConfig.THE_MOVIE_DATABASE_API
        private const val DEFAULT_LANGUAGE = "ru-RU"
    }
}
