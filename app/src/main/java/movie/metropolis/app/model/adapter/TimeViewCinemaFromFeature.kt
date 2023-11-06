package movie.metropolis.app.model.adapter

import movie.core.model.Cinema
import movie.core.model.Showing
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.ProjectionType
import movie.metropolis.app.model.ShowingTag
import movie.metropolis.app.model.SpecificTimeView
import movie.metropolis.app.model.TimeView

data class TimeViewCinemaFromFeature(
    private val model: Cinema,
    private val showings: Iterable<Showing>
) : TimeView.Cinema {
    override val cinema: CinemaView
        get() = CinemaViewFromFeature(model)
    override val times: Map<ShowingTag, List<SpecificTimeView>>
        get() = showings.groupBy {
            ShowingTag(it.language, it.subtitles, it.types.map { ProjectionType(it) })
        }.mapValues { (_, items) ->
            items.map(::SpecificTimeViewFromFeature)
        }
}