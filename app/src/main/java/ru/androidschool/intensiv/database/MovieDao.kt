package ru.androidschool.intensiv.database

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.androidschool.intensiv.database.entities.MovieEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(movie: MovieEntity): Completable

    @Delete
    fun delete(movie: MovieEntity): Completable

    @Query("SELECT * FROM favorMovies")
    fun getMovies(): Single<List<MovieEntity>>

    @Query("SELECT * FROM favorMovies WHERE id=:id")
    fun getMovieById(id: Int): Single<MovieEntity?>
}
