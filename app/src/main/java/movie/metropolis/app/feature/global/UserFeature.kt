package movie.metropolis.app.feature.global

import movie.metropolis.app.feature.global.model.Booking
import movie.metropolis.app.feature.global.model.FieldUpdate
import movie.metropolis.app.feature.global.model.SignInMethod
import movie.metropolis.app.feature.global.model.User

interface UserFeature {

    suspend fun signIn(method: SignInMethod): Result<Unit>
    suspend fun update(data: Iterable<FieldUpdate>): Result<User>
    suspend fun getUser(): Result<User>
    suspend fun getBookings(): Result<Iterable<Booking>>
    suspend fun getToken(): Result<String>

}