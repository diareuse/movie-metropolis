package movie.metropolis.app.presentation.ticket

import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import movie.metropolis.app.model.DataFiltersView
import movie.metropolis.app.model.FiltersView
import movie.metropolis.app.model.ProjectionType
import movie.metropolis.app.model.TimeView
import java.util.Locale

fun List<TimeView>.toFiltersView(): DataFiltersView {
    val filterLanguages = mutableSetOf<FiltersView.Language>()
    val filterTypes = mutableSetOf<FiltersView.Type>()
    for (time in this) {
        for (tag in time.times.keys) {
            filterLanguages += FiltersView.Language(tag.language)
            if (tag.subtitles != null)
                filterLanguages += FiltersView.Language(tag.subtitles)
            filterTypes += tag.projection.map { FiltersView.Type(it) }
        }
    }
    return DataFiltersView(filterLanguages.toImmutableList(), filterTypes.toImmutableList())
}

fun Flow<DataFiltersView>.activate(
    languages: Flow<Set<Locale>>,
    types: Flow<Set<ProjectionType>>
) = combine(this, languages, types) { filters, languages, types ->
    filters.copy(
        languages = filters.languages.map { it.copy(selected = it.locale in languages) }
            .toImmutableList(),
        types = filters.types.map { it.copy(selected = it.type in types) }.toImmutableList()
    )
}

fun <T> MutableStateFlow<Set<T>>.toggle(item: T) {
    return update {
        if (it.contains(item)) it - item else it + item
    }
}
