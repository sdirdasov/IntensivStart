package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName

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
        get() = "https://image.tmdb.org/t/p/w500$field"
}
