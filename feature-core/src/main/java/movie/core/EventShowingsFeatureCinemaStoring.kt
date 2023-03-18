package movie.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import movie.core.adapter.asStored
import movie.core.db.dao.MovieDao
import movie.core.db.dao.MovieReferenceDao
import movie.core.db.dao.ShowingDao
import movie.core.model.Movie
import java.util.Date

class EventShowingsFeatureCinemaStoring(
    private val origin: EventShowingsFeature.Cinema,
    private val movieDao: MovieDao,
    private val referenceDao: MovieReferenceDao,
    private val showingDao: ShowingDao,
    private val scope: CoroutineScope
) : EventShowingsFeature.Cinema {

    override suspend fun get(date: Date) = origin.get(date).onSuccess {
        scope.launch {
            for ((movie, showings) in it) {
                movieDao.insertOrUpdate((movie as Movie).asStored())
                referenceDao.insertOrUpdate(movie.asStored())
                for (showing in showings)
                    showingDao.insertOrUpdate(showing.asStored(movie))
            }
        }
    }

}