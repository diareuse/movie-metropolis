package movie.metropolis.app.model.adapter

import kotlinx.collections.immutable.toImmutableList
import movie.cinema.city.Movie
import movie.cinema.city.Occurrence
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.ProjectionType
import movie.metropolis.app.model.ShowingTag
import movie.metropolis.app.model.SpecificTimeView
import movie.metropolis.app.model.TimeView
import movie.metropolis.app.util.toStringComponents
import java.text.SimpleDateFormat
import java.util.Locale

data class TimeViewMovieFromFeature(
    private val ref: Movie,
    private val showings: Iterable<Occurrence>
) : TimeView.Movie {
    override val movie = MovieView(ref.id).apply {
        val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        name = ref.name.localized
        releasedAt = yearFormat.format(ref.releasedAt)
        duration = ref.length?.toStringComponents().orEmpty()
        availableFrom = releasedAt
        poster = ref.images.first().let(::ImageViewFromMovie)
        posterLarge = poster
        video = ref.videos.firstOrNull()?.let(Any::toString)?.let(::VideoViewFromFeature)
        url = ref.link.toString()
    }
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