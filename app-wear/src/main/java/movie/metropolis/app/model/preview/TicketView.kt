package movie.metropolis.app.model.preview

import androidx.compose.ui.tooling.preview.datasource.*
import movie.metropolis.app.model.TicketView
import java.text.DateFormat
import java.util.Date
import kotlin.random.Random.Default.nextInt
import kotlin.random.Random.Default.nextLong
import kotlin.time.Duration.Companion.days

class TicketViewPreview : CollectionPreviewParameterProvider<TicketView>(
    listOf(
        TicketViewPreview(),
        TicketViewPreview(),
        TicketViewPreview(),
        TicketViewPreview(),
        TicketViewPreview()
    )
) {

    data class TicketViewPreview(
        override val id: String = nextInt().toString(),
        override val cinema: String = "Cinema",
        override val date: String = dateFormat.format(nextDate),
        override val time: String = timeFormat.format(nextDate),
        override val hall: String = "IMAX",
        override val seats: List<TicketView.Seat> = listOf(
            TicketViewSeatPreview(),
            TicketViewSeatPreview()
        ),
        override val name: String = listOf(
            "Ant-Man and the Wasp: Quantumania",
            "Jesus Revolution",
            "Black Panther: Wakanda Forever",
            "Knock at the Cabin"
        ).random()
    ) : TicketView.Active

    data class TicketViewSeatPreview(
        override val row: String = "10",
        override val seat: String = "15"
    ) : TicketView.Seat

    companion object {

        private val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)
        private val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT)
        private val nextDate
            get() = Date(
                nextLong(
                    System.currentTimeMillis() - 30.days.inWholeMilliseconds,
                    System.currentTimeMillis() + 7.days.inWholeMilliseconds
                )
            )

    }

}

class TicketViewPreviews :
    CollectionPreviewParameterProvider<List<TicketView>>(listOf(TicketViewPreview().values.toList()))