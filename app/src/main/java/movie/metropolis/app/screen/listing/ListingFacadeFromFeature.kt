package movie.metropolis.app.screen.listing

import movie.metropolis.app.feature.global.EventFeature
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.adapter.MovieViewFromFeature

class ListingFacadeFromFeature(
    private val event: EventFeature
) : ListingFacade {

    override suspend fun getCurrent(): Result<List<MovieView>> {
        return Result.success(event.getCurrent().getOrThrow().map(::MovieViewFromFeature))
    }

    override suspend fun getUpcoming(): Result<List<MovieView>> {
        return Result.success(event.getUpcoming().getOrThrow().map(::MovieViewFromFeature))
    }

}