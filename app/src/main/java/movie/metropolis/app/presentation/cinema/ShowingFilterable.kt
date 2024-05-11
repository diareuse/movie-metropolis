package movie.metropolis.app.presentation.cinema

import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import movie.metropolis.app.model.AvailabilityView
import movie.metropolis.app.model.Filter

class ShowingFilterable {

    private val selected = MutableStateFlow(persistentListOf<String>())
    private val languages = MutableStateFlow(persistentListOf<String>())
    private val types = MutableStateFlow(persistentListOf<String>())

    val languageFilters
        get() = languages.combine(selected) { languages, selected ->
            languages.map { Filter(it in selected, it) }
        }
    val typeFilters
        get() = types.combine(selected) { languages, selected ->
            languages.map { Filter(it in selected, it) }
        }
    val options = typeFilters.combine(languageFilters) { type, language ->
        mapOf(Filter.Type.Language to language, Filter.Type.Projection to type)
    }

    fun addFrom(types: Iterable<AvailabilityView.Type>) {
        var newTypes = persistentListOf<String>()
        var newLanguages = persistentListOf<String>()
        for (type in types) {
            newTypes = newTypes.addAll(type.types)
            newLanguages = newLanguages.add(type.language)
        }

        this.types.value = newTypes
        this.languages.value = newLanguages

        if (selected.value.isEmpty())
            selected.update { newLanguages }
    }

    fun toggle(filter: Filter) = when (val v = filter.value) {
        in selected.value -> selected.update { it.remove(v) }
        else -> selected.update { it.add(v) }
    }

}