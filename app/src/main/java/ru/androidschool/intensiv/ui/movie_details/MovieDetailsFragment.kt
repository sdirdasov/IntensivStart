package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.movie_details_fragment.*
import kotlinx.android.synthetic.main.movie_details_fragment.movie_rating
import kotlinx.android.synthetic.main.movie_details_header.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.database.MoviesDatabase
import ru.androidschool.intensiv.database.entities.MovieEntity
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.feed.FeedFragment
import ru.androidschool.intensiv.extensions.load
import ru.androidschool.intensiv.extensions.toFloatRating
import ru.androidschool.intensiv.network.responses.MovieDetailsResponse
import ru.androidschool.intensiv.util.applyCompletableAsync
import ru.androidschool.intensiv.util.applyObservableAsync
import timber.log.Timber

class MovieDetailsFragment : Fragment(R.layout.movie_details_fragment) {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private val idMovie by lazy {
        arguments?.getInt(FeedFragment.KEY_MOVIE_ID)
    }

    private var movie: MovieEntity? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getMovieById()
        getMovieActors()

        setFavorCheckbox()
        setFavorCheckedListener()

        movie_details_toolbar.setNavigationIcon(R.drawable.ic_back_button)
        movie_details_toolbar.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setFavorCheckbox() {
        idMovie?.let { id ->
            MoviesDatabase.get(requireContext())?.movieDao()?.let { dbMovies ->
                dbMovies.getMovieById(id)
                    .compose(applyObservableAsync())
                    .subscribe({
                        movie_favor.isChecked = true
                    }, { throwable ->
                        Timber.e(throwable)
                    })
            }
        }
    }

    private fun setFavorCheckedListener() {
        movie_favor.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                saveMovie()
            } else {
                deleteMovie()
            }
        }
    }

    private fun getMovieById() {
        idMovie?.let { id ->
            MovieApiClient.apiClient.getMovieDetailsById(movieId = id)
                .compose(applyObservableAsync())
                .subscribe({ response ->
                    createMovie(id, response)
                    updateView(response)
                }, { throwable ->
                    Timber.e(throwable)
                })
        }
    }

    private fun getMovieActors() {
        idMovie?.let { id ->
            MovieApiClient.apiClient.getActorsMovieById(movieId = id)
                .compose(applyObservableAsync())
                .map { response ->
                    response.cast.map { ActorItem(it) }
                }
                .subscribe({ actorList ->
                    actors_recycler_view.adapter = adapter.apply { addAll(actorList) }
                }, { throwable ->
                    Timber.e(throwable)
                })
        }
    }

    private fun createMovie(id: Int, response: MovieDetailsResponse) {
        movie = MovieEntity(
            id = id,
            title = response.title,
            posterPath = response.posterPath ?: "",
            rating = response.voteAverage.toFloatRating()
        )
    }

    private fun saveMovie() {
        movie?.let {
            MoviesDatabase.get(requireContext())?.movieDao()?.let { dbMovies ->
                dbMovies.save(it)
                    .compose(applyCompletableAsync())
                    .subscribe({ }, { throwable ->
                        movie_favor.isChecked = false
                        Timber.e(throwable)
                    })
            }
        }
    }

    private fun deleteMovie() {
        movie?.let {
            MoviesDatabase.get(requireContext())?.movieDao()?.let { dbMovies ->
                dbMovies.delete(it)
                    .compose(applyCompletableAsync())
                    .subscribe({ }, { throwable ->
                        movie_favor.isChecked = true
                        Timber.e(throwable)
                    })
            }
        }
    }

    private fun updateView(response: MovieDetailsResponse) {
        movie_title.text = response.title
        movie_rating.rating = response.rating
        movie_description.text = response.description
        movie_studio.text = response.studio
        movie_genre.text = response.genres
        movie_year.text = response.date
        movie_image.load(response.posterPath)
    }
}
