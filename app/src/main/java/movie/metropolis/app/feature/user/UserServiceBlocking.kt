package movie.metropolis.app.feature.user

import kotlinx.coroutines.runBlocking
import movie.metropolis.app.feature.user.model.BookingResponse
import movie.metropolis.app.feature.user.model.CustomerDataRequest
import movie.metropolis.app.feature.user.model.CustomerPointsResponse
import movie.metropolis.app.feature.user.model.CustomerResponse
import movie.metropolis.app.feature.user.model.PasswordRequest
import movie.metropolis.app.feature.user.model.RegistrationRequest
import movie.metropolis.app.feature.user.model.TokenRequest
import movie.metropolis.app.feature.user.model.TokenResponse

internal class UserServiceBlocking(
    private val origin: UserService
) : UserService {

    override suspend fun register(request: RegistrationRequest): Result<CustomerResponse> {
        return runBlocking { origin.register(request) }
    }

    override suspend fun getToken(request: TokenRequest): Result<TokenResponse> {
        return runBlocking { origin.getToken(request) }
    }

    override suspend fun updatePassword(request: PasswordRequest): Result<Unit> {
        return runBlocking { origin.updatePassword(request) }
    }

    override suspend fun updateUser(request: CustomerDataRequest): Result<CustomerResponse> {
        return runBlocking { origin.updateUser(request) }
    }

    override suspend fun getPoints(): Result<CustomerPointsResponse> {
        return runBlocking { origin.getPoints() }
    }

    override suspend fun getUser(): Result<CustomerResponse.Customer> {
        return runBlocking { origin.getUser() }
    }

    override suspend fun getBookings(): Result<List<BookingResponse>> {
        return runBlocking { origin.getBookings() }
    }
}