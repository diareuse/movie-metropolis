package movie.metropolis.app.feature.global

import movie.metropolis.app.feature.global.model.BookingDetailResponse
import movie.metropolis.app.feature.global.model.BookingResponse
import movie.metropolis.app.feature.global.model.CustomerDataRequest
import movie.metropolis.app.feature.global.model.CustomerPointsResponse
import movie.metropolis.app.feature.global.model.CustomerResponse
import movie.metropolis.app.feature.global.model.PasswordRequest
import movie.metropolis.app.feature.global.model.RegistrationRequest
import movie.metropolis.app.feature.global.model.TokenRequest
import movie.metropolis.app.feature.global.model.TokenResponse

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
