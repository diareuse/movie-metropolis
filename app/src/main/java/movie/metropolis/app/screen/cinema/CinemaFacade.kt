package movie.metropolis.app.screen.cinema

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.MovieBookingView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.asLoadable
import movie.metropolis.app.screen.cinema.BookingFilterable.Companion.optionsChangedFlow
import java.util.Date

interface CinemaFacade : BookingFilterable {

    suspend fun getCinema(): Result<CinemaView>
    suspend fun getShowings(date: Date): Result<List<MovieBookingView>>

    fun interface Factory {
        fun create(id: String): CinemaFacade
    }

    companion object {

        val CinemaFacade.cinemaFlow
            get() = flow {
                emit(Loadable.loading())
                emit(getCinema().asLoadable())
            }

        fun CinemaFacade.showingsFlow(date: Flow<Date>) = date.flatMapLatest {
            flow {
                emit(Loadable.loading())
                optionsChangedFlow.collect { _ ->
                    emit(getShowings(it).asLoadable())
                }
            }
        }

    }

}