package movie.core.adapter

import movie.core.model.MoviePreview

data class MoviePreviewWithRating(
    private val origin: MoviePreview,
    override val rating: Byte?
) : MoviePreview by origin