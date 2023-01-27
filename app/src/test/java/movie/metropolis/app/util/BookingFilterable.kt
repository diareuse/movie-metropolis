package movie.metropolis.app.util

import movie.metropolis.app.presentation.cinema.BookingFilterable

suspend fun BookingFilterable.disableAll() {
    val options = getOptions().getOrThrow()
        .flatMap { it.value }
        .map { it.copy(isSelected = true) }
    for (option in options)
        toggle(option)
}