package movie.metropolis.app.screen.cinema

import movie.metropolis.app.model.AvailabilityView
import movie.metropolis.app.model.Filter

class ShowingFilterable {

    private val selected = mutableSetOf<String>()
    private val languages = mutableSetOf<String>()
    private val types = mutableSetOf<String>()

    fun addFrom(types: Iterable<AvailabilityView.Type>) {
        this.types.clear()
        this.languages.clear()
        for (type in types) {
            this.types.addAll(type.types)
            this.languages.add(type.language)
        }
    }

    fun selectAll() {
        selected.addAll(languages)
        selected.addAll(types)
    }

    fun toggle(filter: Filter) = when (val v = filter.value) {
        in selected -> selected.remove(v)
        else -> selected.add(v)
    }

    fun getLanguages() = languages
        .map { Filter(it in selected, it) }
        .sortedByDescending { it.isSelected }

    fun getTypes() = types
        .map { Filter(it in selected, it) }
        .sortedByDescending { it.isSelected }

    fun getSelectedTags() = selected.toSet()

}