@file:OptIn(ExperimentalFoundationApi::class)

package movie.metropolis.app.screen2.booking

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import movie.metropolis.app.R
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.screen.cinema.component.CinemaViewParameter
import movie.metropolis.app.screen.detail.MovieDetailViewProvider
import movie.metropolis.app.screen2.booking.component.CardCarousel
import movie.metropolis.app.screen2.booking.component.SeatingRow
import movie.metropolis.app.screen2.booking.component.TicketColumn
import movie.metropolis.app.screen2.booking.component.TicketMetadata
import movie.metropolis.app.screen2.booking.component.TicketMetadataColumn
import movie.style.BackgroundImage
import movie.style.Barcode
import movie.style.Image
import movie.style.layout.PreviewLayout
import movie.style.layout.plus
import movie.style.rememberPaletteImageState
import kotlin.random.Random.Default.nextInt

@Composable
fun BookingScreen(
    bookings: ImmutableList<BookingView>,
    state: PagerState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) = Box(
    modifier = modifier,
    propagateMinConstraints = true
) {
    val background =
        rememberPaletteImageState(url = bookings.getOrNull(state.currentPage)?.movie?.poster?.url)
    BackgroundImage(state = background)
    CardCarousel(
        color = background.palette.color,
        state = state,
        key = { bookings.getOrNull(it)?.id ?: "" },
        contentPadding = contentPadding + PaddingValues(vertical = 32.dp)
    ) {
        val it = bookings[it]
        val state = rememberPaletteImageState(url = it.movie.poster?.url)
        TicketColumn(
            color = state.palette.color,
            contentColor = state.palette.textColor,
            note = null,
            poster = { Image(state) },
            metadata = {
                TicketMetadata(
                    cinema = { Text(it.cinema.name) },
                    date = { Text(it.date) },
                    time = { Text(it.time) },
                    seating = {
                        if (it.seats.isNotEmpty()) SeatingRow(
                            hall = {
                                TicketMetadataColumn(
                                    title = { Text(stringResource(R.string.hall)) }
                                ) {
                                    Text(it.hall)
                                }
                            },
                            row = {
                                TicketMetadataColumn(
                                    title = { Text(stringResource(R.string.row)) }
                                ) {
                                    for (seat in it.seats)
                                        Text(seat.row)
                                }
                            },
                            seat = {
                                TicketMetadataColumn(
                                    title = { Text(stringResource(R.string.seat)) }
                                ) {
                                    for (seat in it.seats)
                                        Text(seat.seat)
                                }
                            }
                        )
                    }
                )
            },
            code = {
                Barcode(
                    modifier = Modifier.background(Color.White),
                    code = it.id
                )
            }
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun BookingScreenPreview() = PreviewLayout {
    val items = BookingParameter().values.toImmutableList()
    val state = rememberPagerState { items.size }
    BookingScreen(items, state)
}

private class BookingParameter(override val count: Int) :
    PreviewParameterProvider<BookingView> {
    constructor() : this(5)

    override val values: Sequence<BookingView> = sequence { repeat(count) { yield(Data()) } }

    private data class Data(
        override val id: String = nextInt().toString(),
        override val name: String = "Spider-man: No way home",
        override val date: String = "22 Feb",
        override val time: String = "12:30",
        override val isPaid: Boolean = true,
        override val movie: MovieDetailView = MovieDetailViewProvider().values.first(),
        override val cinema: CinemaView = CinemaViewParameter().values.first(),
        override val hall: String = "5",
        override val seats: List<BookingView.Seat> = listOf(Seat()),
        override val expired: Boolean = false
    ) : BookingView

    private data class Seat(
        override val row: String = "12",
        override val seat: String = "22"
    ) : BookingView.Seat

}