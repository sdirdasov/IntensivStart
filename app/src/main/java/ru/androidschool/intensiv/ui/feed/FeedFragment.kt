package ru.androidschool.intensiv.ui.feed

import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.extensions.afterTextChanged
import ru.androidschool.intensiv.util.applyObservableAsync
import ru.androidschool.intensiv.util.applyObservableEditText

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

        movies_recycler_view.adapter = adapter

        initSearchObservable()
        getMovies()
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

    private fun initSearchObservable() {
        Observable.create<String> { emitter ->
            search_toolbar.search_edit_text.afterTextChanged {
                emitter.onNext("$it".trim())
            }
        }
            .compose(applyObservableEditText(500))
            .filter {
                it.length > MIN_LENGTH
            }
            .subscribe {
                openSearch(it)
            }
    }

    private fun getMovies() {
        val recommended = MovieApiClient.apiClient.getNowPlayingMovies()
        val upcoming = MovieApiClient.apiClient.getUpcomingMovies()
        val popular = MovieApiClient.apiClient.getPopularMovies()

        Observable.zip(recommended, upcoming, popular,
            { response1, response2, response3 ->
                listOf(
                    FeedItem(R.string.recommended, response1.results),
                    FeedItem(R.string.upcoming, response2.results),
                    FeedItem(R.string.popular, response3.results)
                )
            })
            .compose(applyObservableAsync())
            .doOnSubscribe { progress_bar.visibility = View.VISIBLE }
            .doFinally { progress_bar.visibility = View.GONE }
            .map {
                it.map { feed ->
                    mapToCardContainer(feed.title, feed.items)
                }
            }
            .subscribe {
                it.map { movieList ->
                    adapter.apply { addAll(movieList) }
                }
            }
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
