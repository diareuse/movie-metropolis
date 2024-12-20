package movie.metropolis.app.model

import androidx.compose.runtime.*
import java.util.Locale

@Stable
class FiltersView {

    val activeCount by derivedStateOf { languages.count { it.selected } + types.count { it.selected } }
    val languages = mutableStateListOf<Language>()
    val types = mutableStateListOf<Type>()
    val isEmpty by derivedStateOf { languages.none { it.selected } && types.none { it.selected } }

    operator fun contains(other: List<ProjectionType>): Boolean {
        return types.filter { it.selected }.all { it.type in other }
    }

    operator fun contains(other: Locale?): Boolean {
        return other != null && languages.any { it.selected && it.locale == other }
    }

    @Stable
    data class Language(
        val locale: Locale
    ) {
        var selected by mutableStateOf(false)
    }

    @Immutable
    data class Type(
        val type: ProjectionType
    ) {
        var selected by mutableStateOf(false)
    }

}