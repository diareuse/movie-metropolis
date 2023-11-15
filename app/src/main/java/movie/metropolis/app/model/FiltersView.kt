package movie.metropolis.app.model

import androidx.compose.runtime.*
import kotlinx.collections.immutable.ImmutableList
import java.util.Locale

@Stable
interface FiltersView {

    val activeCount: Int get() = languages.count { it.selected } + types.count { it.selected }
    val languages: ImmutableList<Language>
    val types: ImmutableList<Type>
    val isEmpty: Boolean

    operator fun contains(other: List<ProjectionType>): Boolean
    operator fun contains(other: Locale?): Boolean

    @Immutable
    data class Language(
        val locale: Locale,
        val selected: Boolean = false
    )

    @Immutable
    data class Type(
        val type: ProjectionType,
        val selected: Boolean = false
    )

}