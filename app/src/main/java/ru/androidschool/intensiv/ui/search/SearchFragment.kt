package ru.androidschool.intensiv.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.fragment_search.movies_recycler_view
import kotlinx.android.synthetic.main.fragment_search.progress_bar
import kotlinx.android.synthetic.main.search_toolbar.view.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.extensions.afterTextChanged
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.feed.FeedFragment
import ru.androidschool.intensiv.ui.feed.FeedFragment.Companion.KEY_SEARCH
import ru.androidschool.intensiv.ui.feed.FeedFragment.Companion.MIN_LENGTH
import ru.androidschool.intensiv.ui.feed.MovieItem
import ru.androidschool.intensiv.util.applyObservableAsync
import ru.androidschool.intensiv.util.applyObservableEditText
import timber.log.Timber

class SearchFragment : Fragment(R.layout.fragment_search) {

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
        val searchTerm = requireArguments().getString(KEY_SEARCH)
        search_toolbar.setText(searchTerm)

        movies_recycler_view.adapter = adapter

        searchTerm?.let {
            searchMovie(it)
        }
        initSearchObservable()
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
                adapter.clear()
                searchMovie(it)
            }
    }

    private fun searchMovie(value: String) {
        MovieApiClient.apiClient.searchMovie(query = value)
            .compose(applyObservableAsync())
            .doOnSubscribe { progress_bar.visibility = View.VISIBLE }
            .doFinally { progress_bar.visibility = View.GONE }
            .map { response ->
                response.results.map { movie ->
                    MovieItem(movie) { openMovieDetails(movie) }
                }
            }
            .subscribe({ movieList ->
                adapter.apply { addAll(movieList) }
            }, { throwable ->
                Timber.e(throwable)
            })
    }

    private fun openMovieDetails(movie: Movie) {
        val bundle = Bundle()
        bundle.putInt(FeedFragment.KEY_MOVIE_ID, movie.id)
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }
}
