package movie.metropolis.app.presentation.cinema

import android.location.Location
import kotlinx.coroutines.flow.Flow
import movie.metropolis.app.model.CinemaView

interface CinemasFacade {

    fun cinemas(location: Location?): Flow<List<CinemaView>>

}