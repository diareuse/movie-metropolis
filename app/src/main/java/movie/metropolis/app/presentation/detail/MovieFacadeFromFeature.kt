package movie.metropolis.app.presentation.detail

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import movie.core.EventDetailFeature
import movie.core.EventShowingsFeature
import movie.core.FavoriteFeature
import movie.core.adapter.MovieFromId
import movie.core.model.Location
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.adapter.CinemaBookingViewFromFeature
import movie.metropolis.app.model.adapter.MovieDetailViewFromFeature
import movie.metropolis.app.model.adapter.MoviePreviewFromDetail
import java.util.Date

class MovieFacadeFromFeature(
    id: String,
    private val showings: EventShowingsFeature.Factory,
    private val detail: EventDetailFeature,
    private val favorites: FavoriteFeature
) : MovieFacade {

    private val model = MovieFromId(id)
    private val detailFlow = flow {
        emit(detail.get(model))
    }.shareIn(GlobalScope, SharingStarted.Lazily, 1)

    override val movie: Flow<Result<MovieDetailView>> = detailFlow.map {
        it.map(::MovieDetailViewFromFeature)
    }
    override val favorite = flow {
        emit(favorites.isFavorite(model).getOrDefault(false))
    }
    override val availability = detailFlow.map { result ->
        result.fold({ it.screeningFrom }, { Date() }).coerceAtLeast(Date())
    }

    override fun showings(
        date: Date,
        latitude: Double,
        longitude: Double
    ): Flow<Result<List<CinemaBookingView>>> = flow {
        val location = Location(latitude, longitude)
        val result = showings.movie(model, location).get(date).map { showings ->
            showings.asSequence()
                .map { (cinema, showings) -> CinemaBookingViewFromFeature(cinema, showings) }
                .filter { it.availability.isNotEmpty() }
                .toList()
        }
        emit(result)
    }

    override suspend fun toggleFavorite() {
        val detail = detailFlow.first().getOrNull() ?: return
        val preview = MoviePreviewFromDetail(detail)
        favorites.toggle(preview)
    }

}