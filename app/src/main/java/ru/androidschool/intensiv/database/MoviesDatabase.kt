package ru.androidschool.intensiv.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.androidschool.intensiv.database.entities.MovieEntity

@Database(entities = [MovieEntity::class], version = 2)
abstract class MoviesDatabase : RoomDatabase() {

    companion object {
        private var instance: MoviesDatabase? = null

        fun get(context: Context): MoviesDatabase = instance ?: run {
            Room.databaseBuilder(
                context.applicationContext,
                MoviesDatabase::class.java,
                "movies_db"
            ).fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun movieDao(): MovieDao
}
