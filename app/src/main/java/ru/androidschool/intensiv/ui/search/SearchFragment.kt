package ru.androidschool.intensiv.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.fragment_search.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.feed.FeedFragment.Companion.KEY_SEARCH
import ru.androidschool.intensiv.ui.feed.MovieItem
import timber.log.Timber

class SearchFragment : Fragment(R.layout.fragment_search) {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchTerm = requireArguments().getString(KEY_SEARCH)
        search_toolbar.setText(searchTerm)

        movies_recycler_view.adapter = adapter

        searchTerm?.let {
            MovieApiClient.apiClient.searchMovie(query = it)
                .subscribeOn(Schedulers.io())
                .map { response ->
                    response.results.map { movie ->
                        MovieItem(movie) {}
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ movieList ->
                    adapter.apply { addAll(movieList) }
                }, { throwable ->
                    Timber.e(throwable)
                })
        }

    }
}
