package movie.rating

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import movie.rating.database.RatingDao
import movie.rating.database.RatingStored
import movie.rating.internal.AvailableRating

internal class MetadataProviderStoring(
    private val origin: MetadataProvider,
    private val dao: RatingDao,
    private val scope: CoroutineScope
) : MetadataProvider {

    override suspend fun get(descriptor: MovieDescriptor): AvailableRating? {
        return origin.get(descriptor)?.also {
            scope.launch {
                val stored = RatingStored(descriptor.name, descriptor.year, it.value, it.url)
                dao.insertOrUpdate(stored)
            }
        }
    }

}