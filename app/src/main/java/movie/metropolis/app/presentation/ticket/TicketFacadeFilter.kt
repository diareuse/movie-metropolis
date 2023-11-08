package movie.metropolis.app.presentation.ticket

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import movie.metropolis.app.model.DataFiltersView
import movie.metropolis.app.model.FiltersView
import movie.metropolis.app.model.LazyTimeView
import movie.metropolis.app.model.ShowingTag
import movie.metropolis.app.model.SpecificTimeView
import movie.metropolis.app.model.TimeView

class TicketFacadeFilter(
    private val origin: TicketFacade
) : TicketFacade by origin {

    override val filters: Flow<FiltersView> = origin.filters
        .onStart { emit(DataFiltersView()) }
        .shareIn(GlobalScope, SharingStarted.WhileSubscribed(), 1)

    override val times: Flow<List<LazyTimeView>> = origin.times.flatMapLatest {
        filters.map { filter ->
            it.map { LazyTimeViewFilter(it, filter) }
        }
    }

    private class LazyTimeViewFilter(
        private val origin: LazyTimeView,
        private val filter: FiltersView
    ) : LazyTimeView by origin {
        override val content: Flow<List<TimeView>>
            get() = origin.content.map { times ->
                times.map { time ->
                    when (time) {
                        is TimeView.Cinema -> TimeViewCinemaFilter(time, filter)
                        is TimeView.Movie -> TimeViewMovieFilter(time, filter)
                    }
                }
            }
    }

    private class TimeViewCinemaFilter(
        private val origin: TimeView.Cinema,
        private val filter: FiltersView
    ) : TimeView.Cinema by origin {
        override val times: Map<ShowingTag, List<SpecificTimeView>>
            get() = origin.times.filterKeys { t ->
                var hasLanguage = filter.languages.none { it.selected }
                hasLanguage = hasLanguage or (t.language in filter)
                hasLanguage = hasLanguage or (t.subtitles in filter)
                hasLanguage && t.projection in filter
            }
    }

    private class TimeViewMovieFilter(
        private val origin: TimeView.Movie,
        private val filter: FiltersView
    ) : TimeView.Movie by origin {
        override val times: Map<ShowingTag, List<SpecificTimeView>>
            get() = origin.times.filterKeys { t ->
                var hasLanguage = filter.languages.none { it.selected }
                hasLanguage = hasLanguage or (t.language in filter)
                hasLanguage = hasLanguage or (t.subtitles in filter)
                hasLanguage && t.projection in filter
            }
    }

}