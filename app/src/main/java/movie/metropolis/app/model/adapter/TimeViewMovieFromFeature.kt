package movie.metropolis.app.model.adapter

import movie.core.model.MovieReference
import movie.core.model.Showing
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.ProjectionType
import movie.metropolis.app.model.ShowingTag
import movie.metropolis.app.model.SpecificTimeView
import movie.metropolis.app.model.TimeView

data class TimeViewMovieFromFeature(
    private val ref: MovieReference,
    private val showings: Iterable<Showing>
) : TimeView.Movie {
    override val movie: MovieView
        get() = MovieViewFromReference(ref)
    override val times: Map<ShowingTag, List<SpecificTimeView>>
        get() = showings.groupBy {
            ShowingTag(it.language, it.subtitles, it.types.map { ProjectionType(it) })
        }.mapValues { (_, items) ->
            items.map(::SpecificTimeViewFromFeature)
        }
}