package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.movie_details_fragment.*
import kotlinx.android.synthetic.main.movie_details_fragment.movie_rating
import kotlinx.android.synthetic.main.movie_details_header.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.network.responses.ActorsResponse
import ru.androidschool.intensiv.network.responses.MovieDetailsResponse
import ru.androidschool.intensiv.ui.feed.FeedFragment
import ru.androidschool.intensiv.extensions.formatDate
import ru.androidschool.intensiv.extensions.load
import timber.log.Timber

class MovieDetailsFragment : Fragment(R.layout.movie_details_fragment) {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt(FeedFragment.KEY_MOVIE_ID)

        val getMovieDetails = MovieApiClient.apiClient.getMovieDetailsById(movieId = id!!)
        getMovieDetails.enqueue(object : Callback<MovieDetailsResponse> {
            override fun onResponse(
                call: Call<MovieDetailsResponse>,
                response: Response<MovieDetailsResponse>
            ) {
                response.body()?.let { movie ->
                    movie_title.text = movie.title
                    movie_rating.rating = movie.voteAverage.toFloat() / 2
                    movie_description.text = movie.description
                    movie_studio.text = movie.productionCompanies.joinToString(separator = ", ") { it.name }
                    movie_genre.text = movie.genres.joinToString(separator = ", ") { it.name }
                    movie_year.text = movie.releaseDate.formatDate("yyyy")
                    movie_image.load(movie.posterPath)
                }
            }

            override fun onFailure(call: Call<MovieDetailsResponse>, t: Throwable) {
                Timber.e(t)
            }
        })

        val getActorsMovie = MovieApiClient.apiClient.getActorsMovieById(movieId = id)
        getActorsMovie.enqueue(object : Callback<ActorsResponse> {
            override fun onResponse(
                call: Call<ActorsResponse>,
                response: Response<ActorsResponse>
            ) {
                response.body()?.cast?.let { actors ->
                    val actorList = actors.map { ActorItem(it) }
                    actors_recycler_view.adapter = adapter.apply { addAll(actorList) }
                }
            }

            override fun onFailure(call: Call<ActorsResponse>, t: Throwable) {
                Timber.e(t)
            }
        })

        movie_details_toolbar.setNavigationIcon(R.drawable.ic_back_button)
        movie_details_toolbar.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}
