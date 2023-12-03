package movie.core.preference

interface EventPreference {

    var filterSeen: Boolean
    var onlyMovies: Boolean
    var calendarId: String?
    var distanceKms: Int
    var keywords: List<String>

}