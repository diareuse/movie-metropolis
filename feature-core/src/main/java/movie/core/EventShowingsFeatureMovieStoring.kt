package movie.core

import kotlinx.coroutines.coroutineScope
import movie.core.adapter.asStored
import movie.core.db.dao.ShowingDao
import movie.core.model.Movie
import java.util.Date

class EventShowingsFeatureMovieStoring(
    private val origin: EventShowingsFeature.Movie,
    private val movie: Movie,
    private val showingDao: ShowingDao
) : EventShowingsFeature.Movie {

    override suspend fun get(
        date: Date,
        result: ResultCallback<CinemaWithShowings>
    ) = coroutineScope {
        origin.get(date, result.then(this) {
            for (showing in it.values.flatten())
                showingDao.insertOrUpdate(showing.asStored(movie))
        })
    }

}