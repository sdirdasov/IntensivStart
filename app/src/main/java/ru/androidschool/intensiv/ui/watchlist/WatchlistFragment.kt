package ru.androidschool.intensiv.ui.watchlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_watchlist.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.database.MoviesDatabase
import ru.androidschool.intensiv.util.applyObservableAsync
import timber.log.Timber

class WatchlistFragment : Fragment(R.layout.fragment_watchlist) {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movies_recycler_view.layoutManager = GridLayoutManager(context, 4)

        MoviesDatabase.get(requireContext())?.movieDao()?.let { db ->
            db.getMovies()
                .compose(applyObservableAsync())
                .map { dtoMovies ->
                    dtoMovies.map {
                        Movie(
                            id = it.id,
                            title = it.title,
                            voteAverage = it.rating.toDouble()
                        ).apply { posterPath = it.posterPath }
                    }
                }
                .subscribe({ moviesList ->
                    val list = moviesList.map {
                        MoviePreviewItem(it) { }
                    }
                    movies_recycler_view.adapter = adapter.apply { addAll(list) }
                }, { throwable ->
                    Timber.e(throwable)
                })
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = WatchlistFragment()
    }
}
