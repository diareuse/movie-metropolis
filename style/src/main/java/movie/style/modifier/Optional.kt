package movie.style.modifier

import androidx.compose.ui.*

inline fun <T : Any> Modifier.optional(value: T?, modifier: Modifier.(T) -> Modifier) =
    if (value != null) modifier(this, value) else this