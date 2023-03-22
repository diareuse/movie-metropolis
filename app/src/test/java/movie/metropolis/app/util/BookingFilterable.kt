package movie.metropolis.app.util

import kotlinx.coroutines.flow.first
import movie.metropolis.app.presentation.cinema.BookingFilterable

suspend fun BookingFilterable.disableAll() {
    val options = options.first()
        .flatMap { it.value }
        .filter { it.isSelected }
    for (option in options)
        toggle(option)
}