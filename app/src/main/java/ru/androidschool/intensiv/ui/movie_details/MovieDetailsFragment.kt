package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_details_fragment.*
import kotlinx.android.synthetic.main.movie_details_fragment.movie_rating
import kotlinx.android.synthetic.main.movie_details_header.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.MockRepository
import ru.androidschool.intensiv.ui.feed.FeedFragment

class MovieDetailsFragment : Fragment(R.layout.movie_details_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString(FeedFragment.KEY_TITLE)
        val movieList = MockRepository.getMovies()

        val movie = movieList.find { it.title == title }
        movie?.let {
            movie_title.text = it.title
            movie_rating.rating = it.rating
            movie_description.text = it.description
            movie_studio.text = it.studio
            movie_genre.text = it.genre
            movie_year.text = it.year
        }

        Picasso.get()
            .load("https://m.media-amazon.com/images/M/MV5BYTk3MDljOWQtNGI2My00OTEzLTlhYjQtOTQ4ODM2MzUwY2IwXkEyXkFqcGdeQXVyNTIzOTk5ODM@._V1_.jpg")
            .into(movie_image)

        movie_details_toolbar.setNavigationIcon(R.drawable.ic_back_button)
        movie_details_toolbar.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}
