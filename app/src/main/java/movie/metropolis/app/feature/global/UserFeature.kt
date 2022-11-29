package movie.metropolis.app.feature.global

interface UserFeature {

    suspend fun signIn(method: SignInMethod): Result<Unit>
    suspend fun update(data: Iterable<FieldUpdate>): Result<User>
    suspend fun getUser(): Result<User>
    suspend fun getBookings(): Result<Iterable<Booking>>
    suspend fun getToken(): Result<String>

}