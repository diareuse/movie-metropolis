package movie.calendar

class CalendarListRecover(
    private val origin: CalendarList
) : CalendarList {

    override fun query() = try {
        origin.query()
    } catch (e: Throwable) {
        emptyList()
    }

}