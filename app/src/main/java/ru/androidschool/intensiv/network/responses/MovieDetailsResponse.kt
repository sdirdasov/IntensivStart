package ru.androidschool.intensiv.network.responses

import com.google.gson.annotations.SerializedName
import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.data.Genre
import ru.androidschool.intensiv.data.MovieStudio

data class MovieDetailsResponse(
    @SerializedName("title")
    val title: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("overview")
    val description: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("genres")
    val genres: List<Genre>,
    @SerializedName("production_companies")
    val productionCompanies: List<MovieStudio>
) {
    @SerializedName("poster_path")
    val posterPath: String? = null
        get() = "${BuildConfig.IMAGE_URL}$field"
}
