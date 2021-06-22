package ru.androidschool.intensiv.network.responses

import com.google.gson.annotations.SerializedName
import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.data.Genre
import ru.androidschool.intensiv.data.MovieStudio
import ru.androidschool.intensiv.extensions.formatDate
import ru.androidschool.intensiv.extensions.toFloatRating
import ru.androidschool.intensiv.util.YEAR_PATTERN

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
    val genresList: List<Genre>,
    @SerializedName("production_companies")
    val productionCompanies: List<MovieStudio>
) {
    @SerializedName("poster_path")
    val posterPath: String? = null
        get() = "${BuildConfig.IMAGE_URL}$field"

    val rating: Float
        get() = voteAverage.toFloatRating()

    val date: String
        get() = if (releaseDate.isNotEmpty()) {
            releaseDate.formatDate(YEAR_PATTERN)
        } else { "" }

    val studio: String
        get() = productionCompanies.joinToString(separator = ", ") { it.name }

    val genres: String
        get() = genresList.joinToString(separator = ", ") { it.name }
}
