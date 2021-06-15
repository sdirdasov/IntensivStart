package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName
import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.extensions.toFloatRating

data class Movie(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("vote_average")
    val voteAverage: Double
) {
    @SerializedName("poster_path")
    val posterPath: String? = null
        get() = "${BuildConfig.IMAGE_URL}$field"

    val rating: Float
        get() = voteAverage.toFloatRating()
}
