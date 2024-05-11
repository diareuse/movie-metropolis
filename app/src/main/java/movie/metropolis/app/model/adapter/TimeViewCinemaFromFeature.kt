package movie.metropolis.app.model.adapter

import kotlinx.collections.immutable.toImmutableList
import movie.cinema.city.Cinema
import movie.cinema.city.Occurrence
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.ProjectionType
import movie.metropolis.app.model.ShowingTag
import movie.metropolis.app.model.SpecificTimeView
import movie.metropolis.app.model.TimeView

data class TimeViewCinemaFromFeature(
    private val model: Cinema,
    private val showings: Iterable<Occurrence>
) : TimeView.Cinema {
    override val cinema: CinemaView
        get() = CinemaViewFromFeature(model)
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