package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName

data class TvShow(
    @SerializedName("id")
    val id: Int,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("name")
    val title: String
) {
    @SerializedName("poster_path")
    val posterPath: String? = null
        get() = "https://image.tmdb.org/t/p/w500$field"
}
