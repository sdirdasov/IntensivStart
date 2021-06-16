package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.movie_details_fragment.*
import kotlinx.android.synthetic.main.movie_details_fragment.movie_rating
import kotlinx.android.synthetic.main.movie_details_header.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.feed.FeedFragment
import ru.androidschool.intensiv.extensions.formatDate
import ru.androidschool.intensiv.extensions.load
import timber.log.Timber

class MovieDetailsFragment : Fragment(R.layout.movie_details_fragment) {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private val idMovie by lazy {
        arguments?.getInt(FeedFragment.KEY_MOVIE_ID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getMovieById()
        getMovieActors()

        movie_details_toolbar.setNavigationIcon(R.drawable.ic_back_button)
        movie_details_toolbar.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun getMovieById() {
        idMovie?.let { id ->
            MovieApiClient.apiClient.getMovieDetailsById(movieId = id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    movie_title.text = response.title
                    movie_rating.rating = response.rating
                    movie_description.text = response.description
                    movie_studio.text = response.studio
                    movie_genre.text = response.genres
                    movie_year.text = response.releaseDate.formatDate("yyyy")
                    movie_image.load(response.posterPath)
                }, { throwable ->
                    Timber.e(throwable)
                })
        }
    }

    private fun getMovieActors() {
        idMovie?.let { id ->
            MovieApiClient.apiClient.getActorsMovieById(movieId = id)
                .subscribeOn(Schedulers.io())
                .map { response ->
                    response.cast.map { ActorItem(it) }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ actorList ->
                    actors_recycler_view.adapter = adapter.apply { addAll(actorList) }
                }, { throwable ->
                    Timber.e(throwable)
                })
        }
    }
}
