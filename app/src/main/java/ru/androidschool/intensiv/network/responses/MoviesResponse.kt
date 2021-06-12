package ru.androidschool.intensiv.network.responses

import com.google.gson.annotations.SerializedName
import ru.androidschool.intensiv.data.Movie

data class MoviesResponse(
    val page: Int,
    val results: List<Movie>,
    @SerializedName("total_results")
    var totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)
