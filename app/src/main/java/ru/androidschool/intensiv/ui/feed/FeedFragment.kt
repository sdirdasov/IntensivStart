package ru.androidschool.intensiv.ui.feed

import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.network.responses.MoviesResponse
import ru.androidschool.intensiv.extensions.afterTextChanged
import timber.log.Timber

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

        search_toolbar.search_edit_text.afterTextChanged {
            Timber.d(it.toString())
            if (it.toString().length > MIN_LENGTH) {
                openSearch(it.toString())
            }
        }

        movies_recycler_view.adapter = adapter

        val getNowPlayingMovies = MovieApiClient.apiClient.getNowPlayingMovies()
        getNowPlayingMovies.enqueue(
            provideResponseCallback(R.string.recommended)
        )

        val getUpcomingMovies = MovieApiClient.apiClient.getUpcomingMovies()
        getUpcomingMovies.enqueue(
            provideResponseCallback(R.string.upcoming)
        )

        val getRecommendedMovies = MovieApiClient.apiClient.getPopularMovies()
        getRecommendedMovies.enqueue(
            provideResponseCallback(R.string.popular)
        )
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

    private fun provideResponseCallback(
        @StringRes
        titleContainer: Int
    ) = object : Callback<MoviesResponse> {
        override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
            Timber.e(t)
        }

        override fun onResponse(
            call: Call<MoviesResponse>,
            response: Response<MoviesResponse>
        ) {
            response.body()?.results?.let { results ->
                val moviesList = listOf(
                    MainCardContainer(
                        titleContainer,
                        results.map {
                            MovieItem(it) { movie ->
                                openMovieDetails(movie)
                            }
                        }
                    )
                )
                adapter.apply { addAll(moviesList) }
            }
        }
    }

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
