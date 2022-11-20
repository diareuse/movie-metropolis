package movie.metropolis.app.feature.user

interface UserFeature {

    suspend fun update(data: Iterable<FieldUpdate>): Result<User>
    suspend fun getUser(): Result<User>
    suspend fun getBookings(): Result<Iterable<Booking>>
    suspend fun getToken(): Result<String>

}