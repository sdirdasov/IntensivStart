package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName

data class Actor(
    val name: String
) {
    @SerializedName("profile_path")
    val profilePath: String? = null
        get() = "https://image.tmdb.org/t/p/w500$field"
}
