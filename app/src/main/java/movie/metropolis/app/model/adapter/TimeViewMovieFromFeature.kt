package movie.metropolis.app.model.adapter

import kotlinx.collections.immutable.toImmutableList
import movie.cinema.city.Movie
import movie.cinema.city.Occurrence
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.ProjectionType
import movie.metropolis.app.model.ShowingTag
import movie.metropolis.app.model.SpecificTimeView
import movie.metropolis.app.model.TimeView

data class TimeViewMovieFromFeature(
    private val ref: Movie,
    private val showings: Iterable<Occurrence>
) : TimeView.Movie {
    override val movie: MovieView
        get() = MovieViewFromReference(ref)
    override val times: Map<ShowingTag, List<SpecificTimeView>>
        get() = showings.groupBy {
            ShowingTag(
                it.dubbing,
                it.subtitles,
                it.flags.map { ProjectionType(it.tag) }.toImmutableList()
            )
        }.mapValues { (_, items) ->
            items.map(::SpecificTimeViewFromFeature)
        }
}