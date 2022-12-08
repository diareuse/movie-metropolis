package movie.core

import movie.core.model.Booking
import movie.core.model.FieldUpdate
import movie.core.model.SignInMethod
import movie.core.model.User

interface UserFeature {

    val email: String?

    suspend fun signIn(method: SignInMethod): Result<Unit>
    suspend fun update(data: Iterable<FieldUpdate>): Result<User>
    suspend fun getUser(): Result<User>
    suspend fun getBookings(): Result<Iterable<Booking>>
    suspend fun getToken(): Result<String>

}