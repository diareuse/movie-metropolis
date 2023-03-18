package movie.core.adapter

import movie.core.model.MoviePromoPoster

data class MoviePromoPosterFromDatabase(
    override val url: String
) : MoviePromoPoster