package movie.metropolis.app.model

import androidx.compose.runtime.*
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Immutable
data class ListingView(
    val items: ImmutableList<MovieView>,
    val promotions: ImmutableList<MovieView> = items.take(3).toPersistentList()
)