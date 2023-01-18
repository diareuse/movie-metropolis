package movie.core

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
) : EventShowingsFeature.Cinema {

    override suspend fun get(date: Date, result: ResultCallback<MovieWithShowings>) {
        origin.get(date) { response ->
            result(response)
            response.onSuccess {
                for ((movie, showings) in it) {
                    movieDao.insertOrUpdate((movie as Movie).asStored())
                    referenceDao.insertOrUpdate(movie.asStored())
                    for (showing in showings)
                        showingDao.insertOrUpdate(showing.asStored(movie))
                }
            }
        }
    }

}