package movie.metropolis.app.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.Locale

data class DataFiltersView(
    override val languages: ImmutableList<FiltersView.Language> = persistentListOf(),
    override val types: ImmutableList<FiltersView.Type> = persistentListOf()
) : FiltersView {

    override val isEmpty get() = languages.none { it.selected } && types.none { it.selected }

    override operator fun contains(other: Locale?): Boolean {
        return other != null && languages.any { it.selected && it.locale == other }
    }

    override operator fun contains(other: List<ProjectionType>): Boolean {
        return types.filter { it.selected }.all { it.type in other }
    }

}