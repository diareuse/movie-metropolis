package movie.core.adapter

import movie.core.model.MoviePromoPoster

data class MoviePromoPosterWithSpotColor(
    private val origin: MoviePromoPoster,
    override val spotColor: Int
) : MoviePromoPoster by origin