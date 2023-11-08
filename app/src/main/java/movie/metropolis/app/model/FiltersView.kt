package movie.metropolis.app.model

import java.util.Locale

interface FiltersView {

    val languages: List<Language>
    val types: List<Type>
    val isEmpty: Boolean

    operator fun contains(other: List<ProjectionType>): Boolean
    operator fun contains(other: Locale?): Boolean

    data class Language(
        val locale: Locale,
        val selected: Boolean = false
    )

    data class Type(
        val type: ProjectionType,
        val selected: Boolean = false
    )

}