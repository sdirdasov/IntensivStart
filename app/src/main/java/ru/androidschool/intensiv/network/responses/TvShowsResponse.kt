package ru.androidschool.intensiv.network.responses

import com.google.gson.annotations.SerializedName
import ru.androidschool.intensiv.data.TvShow

data class TvShowsResponse(
    val page: Int,
    val results: List<TvShow>,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)
