package movie.metropolis.app.feature.user

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
    suspend fun getToken(refreshKey: String): Result<TokenResponse>

    suspend fun updatePassword(data: PasswordRequest): Result<Unit>
    suspend fun updateUser(data: CustomerDataRequest): Result<CustomerResponse>
    suspend fun getPoints(): Result<CustomerPointsResponse>
    suspend fun getUser(): Result<CustomerResponse.Customer>
    suspend fun getBookings(): Result<List<BookingResponse>>

}
