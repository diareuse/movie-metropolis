package movie.rating

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import movie.rating.internal.AvailableRating
import movie.rating.internal.ComposedRating
import movie.rating.internal.RatingType
import movie.rating.internal.RatingType.Csfd
import movie.rating.internal.RatingType.Imdb
import movie.rating.internal.RatingType.RottenTomatoes

internal class MetadataProviderComposed(
    private val rtt: MetadataProvider,
    private val imdb: MetadataProvider,
    private val csfd: MetadataProvider
) : MetadataProvider.Composed {

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

    private suspend fun MetadataProvider.getOrNull(descriptor: MovieDescriptor): AvailableRating? =
        kotlin.runCatching { get(descriptor) }.getOrNull()

    private suspend fun MetadataProvider.get(vararg descriptors: MovieDescriptor): AvailableRating? =
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