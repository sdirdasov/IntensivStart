package ru.androidschool.intensiv.network.responses

import com.google.gson.annotations.SerializedName

data class MoviesResponse<T>(
    val page: Int,
    val results: List<T>,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)
