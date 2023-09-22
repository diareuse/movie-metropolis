package movie.metropolis.app.model

sealed interface TicketView {
    val id: String
    val cinema: String
    val date: String
    val time: String
    val name: String

    interface Seat {
        val row: String
        val seat: String
    }

    interface Expired : TicketView
    interface Active : TicketView {
        val hall: String
        val seats: List<Seat>
    }

}