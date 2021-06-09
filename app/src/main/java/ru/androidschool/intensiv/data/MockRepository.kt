package ru.androidschool.intensiv.data

object MockRepository {

    fun getMovies(): List<Movie> {

        val moviesList = mutableListOf<Movie>()
        for (x in 0..10) {
            val movie = Movie(
                title = "Spider-Man $x",
                voteAverage = 10.0 - x,
                description = "The film centers on outcast teen genius Peter Parker, who develops spider-like superhuman abilities after being bitten by a genetically-altered spider, and decides to use his newfound powers to fight crime and protect the people of New York City as Spider-Man.",
                studio = "Warner Bros.",
                genre = "Action, Adventure, Fantasy",
                year = "2003"
            )
            moviesList.add(movie)
        }

        return moviesList
    }

    fun getTvShows(): List<TvShow> {

        val tvShowList = mutableListOf<TvShow>()
        for (x in 0..10) {
            val tvShow = TvShow(
                title = "Stranger things $x",
                voteAverage = 10.0 - x
            )
            tvShowList.add(tvShow)
        }

        return tvShowList
    }
}
