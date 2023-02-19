package movie.metropolis.app.util

import movie.metropolis.app.presentation.cinema.BookingFilterable

suspend fun BookingFilterable.disableAll() {
    val options = getOptions().getOrThrow()
        .flatMap { it.value }
        .filter { it.isSelected }
    for (option in options)
        toggle(option)
}