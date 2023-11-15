@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package movie.metropolis.app.screen2.ticket

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
import kotlinx.collections.immutable.toImmutableList
import movie.metropolis.app.R
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.screen.cinema.component.CinemaViewParameter
import movie.metropolis.app.screen.detail.MovieDetailViewProvider
import movie.metropolis.app.screen2.ticket.component.CardCarousel
import movie.metropolis.app.screen2.ticket.component.PageIndicator
import movie.metropolis.app.screen2.ticket.component.SeatingRow
import movie.metropolis.app.screen2.ticket.component.TicketColumn
import movie.metropolis.app.screen2.ticket.component.TicketMetadata
import movie.metropolis.app.screen2.ticket.component.TicketMetadataColumn
import movie.metropolis.app.screen2.ticket.component.VerticalActions
import movie.metropolis.app.screen2.ticket.component.rememberAnchoredDraggableState
import movie.style.BackgroundImage
import movie.style.Barcode
import movie.style.Image
import movie.style.imagePlaceholder
import movie.style.layout.PreviewLayout
import movie.style.layout.largeScreenPadding
import movie.style.layout.plus
import movie.style.modifier.screenBrightness
import movie.style.rememberPaletteImageState
import movie.style.textPlaceholder
import kotlin.random.Random.Default.nextInt

enum class TicketAnchor { Idle, Share }

@Composable
fun TicketScreen(
    bookings: TicketContentState,
    state: PagerState,
    indicatorState: PagerState,
    onShareClick: (BookingView) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) = Box(
    modifier = modifier,
    propagateMinConstraints = true
) {
    val background =
        rememberPaletteImageState(
            url = bookings.asSuccess()?.getOrNull(state.currentPage)?.movie?.poster?.url
        )
    BackgroundImage(state = background)
    var fullBrightness by remember { mutableStateOf(false) }
    var largeScreenPadding by remember { mutableStateOf(PaddingValues(0.dp)) }
    Column(
        modifier = Modifier
            .padding(contentPadding)
            .statusBarsPadding()
            .padding(bottom = 16.dp)
            .largeScreenPadding(widthAtMost = 400.dp) { largeScreenPadding = it },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PageIndicator(
            state = indicatorState,
            color = background.palette.color
        )
        CardCarousel(
            modifier = Modifier
                .weight(1f)
                .screenBrightness(full = fullBrightness),
            state = state,
            key = { bookings.asSuccess()?.getOrNull(it)?.id ?: "" },
            contentPadding = PaddingValues(vertical = 8.dp) + largeScreenPadding
        ) {
            val items = bookings.asSuccess()
            if (items != null) {
                val it = items[it]
                val state = rememberPaletteImageState(url = it.movie.poster?.url)
                val note: (@Composable () -> Unit)? =
                    if (it.expired) ({ Text(stringResource(id = R.string.expired)) })
                    else null
                VerticalActions(
                    state = rememberAnchoredDraggableState(initialValue = TicketAnchor.Idle),
                    enabled = !it.expired,
                    actions = {
                        IconButton(
                            modifier = Modifier.anchor(TicketAnchor.Share),
                            onClick = { onShareClick(it) }
                        ) {
                            Icon(painterResource(R.drawable.ic_share), null)
                        }
                    }
                ) {
                    TicketColumn(
                        color = state.palette.color,
                        contentColor = state.palette.textColor,
                        note = note,
                        poster = { Image(state, alignment = Alignment.BottomCenter) },
                        metadata = {
                            TicketMetadata(
                                name = { Text(it.movie.name) },
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
                                modifier = Modifier
                                    .background(Color.White)
                                    .clickable { fullBrightness = !fullBrightness },
                                code = it.id
                            )
                        }
                    )
                }
            } else if (bookings.isLoading) {
                TicketColumn(
                    note = null,
                    poster = { Box(Modifier.imagePlaceholder(RectangleShape)) },
                    metadata = {
                        val placeholder = Modifier.textPlaceholder()
                        TicketMetadata(
                            name = { Text("#".repeat(10), modifier = placeholder) },
                            cinema = { Text("#".repeat(10), modifier = placeholder) },
                            date = { Text("#".repeat(12), modifier = placeholder) },
                            time = { Text("#".repeat(5), modifier = placeholder) },
                            seating = {
                                SeatingRow(
                                    hall = {
                                        TicketMetadataColumn(
                                            title = { Text(stringResource(R.string.hall)) }
                                        ) {
                                            Text("#".repeat(6), modifier = placeholder)
                                        }
                                    },
                                    row = {
                                        TicketMetadataColumn(
                                            title = { Text(stringResource(R.string.row)) }
                                        ) {
                                            Text("#".repeat(2), modifier = placeholder)
                                        }
                                    },
                                    seat = {
                                        TicketMetadataColumn(
                                            title = { Text(stringResource(R.string.seat)) }
                                        ) {
                                            Text("#".repeat(2), modifier = placeholder)
                                        }
                                    }
                                )
                            }
                        )
                    },
                    code = {
                        Box(
                            modifier = Modifier
                                .background(Color.White)
                                .imagePlaceholder(RectangleShape)
                        )
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun BookingScreenPreview() = PreviewLayout {
    val items = BookingParameter().values.toImmutableList()
    val state = rememberPagerState { items.size }
    TicketScreen(TicketContentState.Success(items), state, state, {})
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun BookingScreenLoadingPreview() = PreviewLayout {
    val state = rememberPagerState { 1 }
    TicketScreen(TicketContentState.Loading, state, state, {})
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
    ) : BookingView {
        override fun origin(): BookingView {
            return this
        }
    }

    private data class Seat(
        override val row: String = "12",
        override val seat: String = "22"
    ) : BookingView.Seat

}