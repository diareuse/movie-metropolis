package movie.core.adapter

import movie.core.model.MoviePreview

internal data class MoviePreviewWithSpotColor(
    private val origin: MoviePreview,
    override val spotColor: Int
) : MoviePreview by origin