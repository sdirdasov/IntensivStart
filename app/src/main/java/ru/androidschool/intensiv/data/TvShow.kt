package ru.androidschool.intensiv.data

data class TvShow(
    var title: String? = "",
    var voteAverage: Double = 0.0
) {
    val rating: Float
        get() = voteAverage.div(2).toFloat()
}
