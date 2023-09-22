package movie.metropolis.app.model.adapter

import android.annotation.SuppressLint
import com.google.android.gms.wearable.DataMap
import movie.metropolis.app.model.TicketView
import java.text.DateFormat
import java.util.Date

@SuppressLint("VisibleForTests")
data class TicketViewExpiredFromDataMap(
    private val map: DataMap
) : TicketView.Expired {

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
    override val name: String
        get() = map.getString("name", "")

}