package ru.androidschool.intensiv.ui.feed

import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.extensions.afterTextChanged
import timber.log.Timber
import java.util.concurrent.TimeUnit

class FeedFragment : Fragment(R.layout.feed_fragment) {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private val options = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Observable.create<String> { emitter ->
            search_toolbar.search_edit_text.afterTextChanged {
                emitter.onNext("$it".trim())
            }
        }
            .debounce(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .filter {
                it.length > MIN_LENGTH
            }
            .subscribe{
                openSearch(it)
            }

        movies_recycler_view.adapter = adapter

        getRecommendedMovies()
        getUpcomingMovies()
        getPopularMovies()
    }

    private fun openMovieDetails(movie: Movie) {
        val bundle = Bundle()
        bundle.putInt(KEY_MOVIE_ID, movie.id)
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }

    private fun openSearch(searchText: String) {
        val bundle = Bundle()
        bundle.putString(KEY_SEARCH, searchText)
        findNavController().navigate(R.id.search_dest, bundle, options)
    }

    private fun getRecommendedMovies() {
        MovieApiClient.apiClient.getNowPlayingMovies()
            .subscribeOn(Schedulers.io())
            .map { response ->
                mapToCardContainer(R.string.recommended, response.results)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ moviesList ->
                adapter.apply { addAll(moviesList) }
            }, { throwable ->
                Timber.e(throwable)
            })
    }

    private fun getUpcomingMovies() {
        MovieApiClient.apiClient.getUpcomingMovies()
            .subscribeOn(Schedulers.io())
            .map { response ->
                mapToCardContainer(R.string.upcoming, response.results)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ moviesList ->
                adapter.apply { addAll(moviesList) }
            }, { throwable ->
                Timber.e(throwable)
            })
    }

    private fun getPopularMovies() {
        MovieApiClient.apiClient.getPopularMovies()
            .subscribeOn(Schedulers.io())
            .map { response ->
                mapToCardContainer(R.string.popular, response.results)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ moviesList ->
                adapter.apply { addAll(moviesList) }
            }, { throwable ->
                Timber.e(throwable)
            })
    }

    private fun mapToCardContainer(
        @StringRes titleContainer: Int,
        movies: List<Movie>
    ): List<MainCardContainer> = listOf(
        MainCardContainer(
            titleContainer,
            movies.map {
                MovieItem(it) { movie ->
                    openMovieDetails(movie)
                }
            }
        )
    )

    override fun onStop() {
        super.onStop()
        search_toolbar.clear()
        adapter.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    companion object {
        const val MIN_LENGTH = 3
        const val KEY_SEARCH = "search"
        const val KEY_MOVIE_ID = "id"
    }
}
