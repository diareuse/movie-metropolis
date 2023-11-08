package movie.metropolis.app.model

import java.util.Locale

data class DataFiltersView(
    override val languages: List<FiltersView.Language> = emptyList(),
    override val types: List<FiltersView.Type> = emptyList()
) : FiltersView {

    override val isEmpty get() = languages.none { it.selected } && types.none { it.selected }

    override operator fun contains(other: Locale?): Boolean {
        return other != null && languages.any { it.selected && it.locale == other }
    }

    override operator fun contains(other: List<ProjectionType>): Boolean {
        return types.filter { it.selected }.all { it.type in other }
    }

}