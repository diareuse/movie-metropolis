package movie.core.nwk

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movie.core.nwk.model.CustomerDataRequest
import movie.core.nwk.model.PasswordRequest
import movie.core.nwk.model.RegistrationRequest
import movie.core.nwk.model.TokenRequest

class UserServiceThread(
    private val origin: UserService
) : UserService {

    override suspend fun register(request: RegistrationRequest) = withContext(Dispatchers.IO) {
        origin.register(request)
    }

    override suspend fun getToken(request: TokenRequest) = withContext(Dispatchers.IO) {
        origin.getToken(request)
    }

    override suspend fun getCurrentToken() = withContext(Dispatchers.IO) {
        origin.getCurrentToken()
    }

    override suspend fun updatePassword(request: PasswordRequest) = withContext(Dispatchers.IO) {
        origin.updatePassword(request)
    }

    override suspend fun updateUser(request: CustomerDataRequest) = withContext(Dispatchers.IO) {
        origin.updateUser(request)
    }

    override suspend fun getPoints() = withContext(Dispatchers.IO) {
        origin.getPoints()
    }

    override suspend fun getUser() = withContext(Dispatchers.IO) {
        origin.getUser()
    }

    override suspend fun getBookings() = withContext(Dispatchers.IO) {
        origin.getBookings()
    }

    override suspend fun getBooking(id: String) = withContext(Dispatchers.IO) {
        origin.getBooking(id)
    }
}