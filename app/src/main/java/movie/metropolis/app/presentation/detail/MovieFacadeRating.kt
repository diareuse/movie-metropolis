package movie.metropolis.app.presentation.detail

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.adapter.MovieDetailViewWithRating
import movie.rating.MetadataProvider
import movie.rating.MovieDescriptor
import movie.rating.MovieMetadata
import java.util.Calendar

class MovieFacadeRating(
    private val origin: MovieFacade,
    private val rating: MetadataProvider
) : MovieFacade by origin {

    private var metadata: MovieMetadata? = null

    override val movie: Flow<MovieDetailView> = origin.movie.flatMapLatest {
        flow {
            if (metadata == null) emit(it)
            val year = Calendar.getInstance().apply { time = it.base().releasedAt }[Calendar.YEAR]
            val descriptors = arrayOf(
                MovieDescriptor.Original(it.nameOriginal, year),
                MovieDescriptor.Local(it.name, year)
            )
            val rating = metadata ?: descriptors.fold(null as MovieMetadata?) { acc, it ->
                acc ?: rating.get(it)
            }
            metadata = rating
            emit(MovieDetailViewWithRating(it, rating))
        }
    }

}