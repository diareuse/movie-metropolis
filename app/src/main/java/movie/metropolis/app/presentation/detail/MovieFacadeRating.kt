package movie.metropolis.app.presentation.detail

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.adapter.MovieDetailViewWithRating
import movie.metropolis.app.util.flatMapResult
import movie.rating.MetadataProvider
import movie.rating.MovieDescriptor
import java.util.Calendar

class MovieFacadeRating(
    private val origin: MovieFacade,
    private val rating: MetadataProvider.Composed
) : MovieFacade by origin {

    override val movie: Flow<Result<MovieDetailView>> = origin.movie.flatMapResult {
        flow {
            emit(it)
            val year = Calendar.getInstance().apply { time = it.base().releasedAt }[Calendar.YEAR]
            val descriptors = arrayOf(
                MovieDescriptor.Original(it.nameOriginal, year),
                MovieDescriptor.Local(it.name, year)
            )
            val rating = rating.get(descriptors = descriptors)
            emit(MovieDetailViewWithRating(it, rating))
        }.map(Result.Companion::success)
    }

}