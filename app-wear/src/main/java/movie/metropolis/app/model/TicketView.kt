package movie.metropolis.app.model

interface TicketView {
    val id: String
    val cinema: String
    val date: String
    val time: String
    val hall: String
    val seats: List<Seat>
    val image: String?

    interface Seat {
        val row: String
        val seat: String
    }
}