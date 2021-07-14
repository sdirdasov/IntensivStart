package ru.androidschool.intensiv.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorMovies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val posterPath: String,
    val rating: Float
)
