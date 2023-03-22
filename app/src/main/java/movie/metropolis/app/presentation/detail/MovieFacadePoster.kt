package movie.metropolis.app.presentation.detail

import androidx.compose.ui.graphics.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import movie.image.ImageAnalyzer
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.adapter.ImageViewWithColor
import movie.metropolis.app.model.adapter.MovieDetailViewWithColor
import movie.metropolis.app.util.flatMapResult

class MovieFacadePoster(
    private val origin: MovieFacade,
    private val analyzer: ImageAnalyzer
) : MovieFacade by origin {

    override val movie: Flow<Result<MovieDetailView>> = origin.movie.flatMapResult {
        flow {
            emit(it)
            var poster = it.poster ?: return@flow
            val color = analyzer.getColors(poster.url).vibrant.rgb.let(::Color)
            poster = ImageViewWithColor(poster, color)
            emit(MovieDetailViewWithColor(it, poster))
        }.map(Result.Companion::success)
    }

}