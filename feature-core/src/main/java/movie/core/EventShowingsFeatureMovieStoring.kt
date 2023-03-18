package movie.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import movie.core.adapter.asStored
import movie.core.db.dao.ShowingDao
import movie.core.model.Movie
import java.util.Date

// fixme this is faulty due to the fact we only check data is empty when loading from database, once one movie loads then it's not empty and fucks the experience royally
class EventShowingsFeatureMovieStoring(
    private val origin: EventShowingsFeature.Movie,
    private val movie: Movie,
    private val showingDao: ShowingDao,
    private val scope: CoroutineScope
) : EventShowingsFeature.Movie {

    override suspend fun get(date: Date) = origin.get(date).onSuccess {
        scope.launch {
            for (showing in it.values.flatten())
                showingDao.insertOrUpdate(showing.asStored(movie))
        }
    }

}