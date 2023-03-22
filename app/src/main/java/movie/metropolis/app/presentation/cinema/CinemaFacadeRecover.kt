package movie.metropolis.app.presentation.cinema

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import movie.core.Recoverable
import movie.metropolis.app.model.CinemaView
import java.util.Date

class CinemaFacadeRecover(
    private val origin: CinemaFacade
) : CinemaFacade by origin, Recoverable {

    override val cinema: Flow<Result<CinemaView>> = origin.cinema
        .catch { emit(Result.failure(it)) }

    override fun showings(date: Date) = origin.showings(date)
        .catch { emit(Result.failure(it)) }

}