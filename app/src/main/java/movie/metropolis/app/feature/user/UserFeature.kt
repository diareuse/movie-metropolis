package movie.metropolis.app.feature.user

interface UserFeature {

    suspend fun update(data: Iterable<FieldUpdate>): Result<User>
    suspend fun get(): Result<User>
    suspend fun getBookings(): Result<Iterable<Booking>>

}