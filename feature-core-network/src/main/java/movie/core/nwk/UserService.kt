package movie.core.nwk

import movie.core.nwk.model.BookingDetailResponse
import movie.core.nwk.model.BookingResponse
import movie.core.nwk.model.CustomerDataRequest
import movie.core.nwk.model.CustomerPointsResponse
import movie.core.nwk.model.CustomerResponse
import movie.core.nwk.model.PasswordRequest
import movie.core.nwk.model.RegistrationRequest
import movie.core.nwk.model.TokenRequest
import movie.core.nwk.model.TokenResponse

interface UserService {

    suspend fun register(request: RegistrationRequest): Result<CustomerResponse>
    suspend fun getToken(request: TokenRequest): Result<TokenResponse>
    suspend fun getCurrentToken(): Result<String>

    suspend fun updatePassword(request: PasswordRequest): Result<Unit>
    suspend fun updateUser(request: CustomerDataRequest): Result<CustomerResponse>
    suspend fun getPoints(): Result<CustomerPointsResponse>
    suspend fun getUser(): Result<CustomerResponse.Customer>
    suspend fun getBookings(): Result<List<BookingResponse>>
    suspend fun getBooking(id: String): Result<BookingDetailResponse>

}
