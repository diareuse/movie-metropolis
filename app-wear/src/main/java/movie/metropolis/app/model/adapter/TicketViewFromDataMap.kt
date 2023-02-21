package movie.metropolis.app.model.adapter

import com.google.android.gms.wearable.DataMap
import movie.metropolis.app.model.TicketView
import java.text.DateFormat
import java.util.Date

data class TicketViewFromDataMap(
    private val map: DataMap
) : TicketView {

    private val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)
    private val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT)

    override val id: String
        get() = map.getString("id", "")
    override val cinema: String
        get() = map.getString("cinema", "")
    override val date: String
        get() = dateFormat.format(Date(map.getLong("starts_at", 0)))
    override val time: String
        get() = timeFormat.format(Date(map.getLong("starts_at", 0)))
    override val hall: String
        get() = map.getString("hall", "")
    override val seats: List<TicketView.Seat>
        get() = map.getDataMapArrayList("seats").orEmpty()
            .map(TicketViewFromDataMap::SeatFromDataMap)
    override val name: String
        get() = map.getString("name", "")

    data class SeatFromDataMap(
        private val map: DataMap
    ) : TicketView.Seat {

        override val row: String
            get() = map.getString("row", "")
        override val seat: String
            get() = map.getString("seat", "")

    }

}