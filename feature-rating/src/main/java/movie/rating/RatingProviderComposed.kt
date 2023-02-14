package movie.rating

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import movie.rating.RatingType.Csfd
import movie.rating.RatingType.Imdb
import movie.rating.RatingType.RottenTomatoes

internal class RatingProviderComposed(
    private val rtt: RatingProvider,
    private val imdb: RatingProvider,
    private val csfd: RatingProvider
) : RatingProvider.Composed {

    override suspend fun get(vararg descriptors: MovieDescriptor): ComposedRating {
        val output = mutableMapOf<RatingType, AvailableRating>()
        val original = descriptors.filterIsInstance<MovieDescriptor.Original>().toTypedArray()
        val local = descriptors.filterIsInstance<MovieDescriptor.Local>().toTypedArray()
        coroutineScope {
            launch { output[Csfd] = csfd.get(descriptors = local) ?: return@launch }
            launch { output[Imdb] = imdb.get(descriptors = original) ?: return@launch }
            launch { output[RottenTomatoes] = rtt.get(descriptors = original) ?: return@launch }
        }
        return ComposedRatingMap(output)
    }

    private suspend fun RatingProvider.getOrNull(descriptor: MovieDescriptor): AvailableRating? =
        kotlin.runCatching { get(descriptor) }.getOrNull()

    private suspend fun RatingProvider.get(vararg descriptors: MovieDescriptor): AvailableRating? =
        descriptors.firstNotNullOfOrNull { getOrNull(it) }

    private data class ComposedRatingMap(
        private val values: Map<RatingType, AvailableRating>
    ) : ComposedRating {

        override val imdb: AvailableRating?
            get() = values[Imdb]
        override val rottenTomatoes: AvailableRating?
            get() = values[RottenTomatoes]
        override val csfd: AvailableRating?
            get() = values[Csfd]

    }

}