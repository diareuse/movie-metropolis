package movie.metropolis.app.feature.global

import movie.metropolis.app.feature.global.model.remote.BookingDetailResponse
import movie.metropolis.app.feature.global.model.remote.BookingResponse
import movie.metropolis.app.feature.global.model.remote.CustomerDataRequest
import movie.metropolis.app.feature.global.model.remote.CustomerPointsResponse
import movie.metropolis.app.feature.global.model.remote.CustomerResponse
import movie.metropolis.app.feature.global.model.remote.PasswordRequest
import movie.metropolis.app.feature.global.model.remote.RegistrationRequest
import movie.metropolis.app.feature.global.model.remote.TokenRequest
import movie.metropolis.app.feature.global.model.remote.TokenResponse

internal interface UserService {

    suspend fun register(request: RegistrationRequest): Result<CustomerResponse>
    suspend fun getToken(request: TokenRequest): Result<TokenResponse>

    suspend fun updatePassword(request: PasswordRequest): Result<Unit>
    suspend fun updateUser(request: CustomerDataRequest): Result<CustomerResponse>
    suspend fun getPoints(): Result<CustomerPointsResponse>
    suspend fun getUser(): Result<CustomerResponse.Customer>
    suspend fun getBookings(): Result<List<BookingResponse>>
    suspend fun getBooking(id: String): Result<BookingDetailResponse>

}
