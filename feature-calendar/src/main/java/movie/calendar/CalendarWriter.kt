package movie.calendar

interface CalendarWriter {

    fun write(metadata: EventMetadata)

    fun interface Factory {

        fun create(calendarId: String): CalendarWriter

    }

}

