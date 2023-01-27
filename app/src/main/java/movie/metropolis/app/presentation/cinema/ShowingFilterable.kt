package movie.metropolis.app.presentation.cinema

import movie.metropolis.app.model.AvailabilityView
import movie.metropolis.app.model.Filter

class ShowingFilterable {

    private val selected = mutableSetOf<String>()
    private val languages = mutableSetOf<String>()
    private val types = mutableSetOf<String>()

    fun addFrom(types: Iterable<AvailabilityView.Type>): Boolean {
        val elements = this.types + this.languages
        this.types.clear()
        this.languages.clear()
        for (type in types) {
            this.types.addAll(type.types)
            this.languages.add(type.language)
        }
        val updatedElements = this.types + this.languages
        return (elements intersect updatedElements).size != elements.size
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

    fun getSelectedLanguages() = selected.filter { it in languages }.toSet()
    fun getSelectedTypes() = selected.filter { it in types }.toSet()

}