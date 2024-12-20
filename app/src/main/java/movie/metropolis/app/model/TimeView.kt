package movie.metropolis.app.model

import androidx.compose.runtime.*

@Stable
sealed class TimeView {

    abstract val filters: FiltersView
    val times = mutableStateMapOf<ShowingTag, List<SpecificTimeView>>()
    val filteredTimes by derivedStateOf {
        times.filterKeys { t ->
            var hasLanguage = filters.languages.none { it.selected }
            hasLanguage = hasLanguage or (t.language in filters)
            hasLanguage = hasLanguage or (t.subtitles in filters)
            hasLanguage && t.projection in filters
        }
    }

    @Stable
    data class Cinema(
        val cinema: CinemaView,
        override val filters: FiltersView
    ) : TimeView()

    @Stable
    data class Movie(
        val movie: MovieView,
        override val filters: FiltersView
    ) : TimeView()

}