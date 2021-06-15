package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName
import ru.androidschool.intensiv.BuildConfig

data class Actor(
    val name: String
) {
    @SerializedName("profile_path")
    val profilePath: String? = null
        get() = "${BuildConfig.IMAGE_URL}$field"
}
