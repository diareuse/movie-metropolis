package movie.metropolis.app.feature.user

import movie.metropolis.app.feature.user.model.BookingDetailResponse
import movie.metropolis.app.feature.user.model.BookingResponse
import movie.metropolis.app.feature.user.model.CustomerDataRequest
import movie.metropolis.app.feature.user.model.CustomerPointsResponse
import movie.metropolis.app.feature.user.model.CustomerResponse
import movie.metropolis.app.feature.user.model.PasswordRequest
import movie.metropolis.app.feature.user.model.RegistrationRequest
import movie.metropolis.app.feature.user.model.TokenRequest
import movie.metropolis.app.feature.user.model.TokenResponse

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
