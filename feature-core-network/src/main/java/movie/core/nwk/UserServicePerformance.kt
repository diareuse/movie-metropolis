package movie.core.nwk

import movie.core.nwk.model.CustomerDataRequest
import movie.core.nwk.model.PasswordRequest
import movie.core.nwk.model.RegistrationRequest
import movie.core.nwk.model.TokenRequest

class UserServicePerformance(
    private val origin: UserService,
    private val tracer: PerformanceTracer
) : UserService {

    override suspend fun register(request: RegistrationRequest) = tracer.trace("api.register") {
        origin.register(request).also { result ->
            it.setState(result.isSuccess)

        }
    }

    override suspend fun getToken(request: TokenRequest) = tracer.trace("api.token") {
        origin.getToken(request).also { result ->
            it.setState(result.isSuccess)
        }
    }

    override suspend fun getCurrentToken() = tracer.trace("api.current-token") {
        origin.getCurrentToken().also { result ->
            it.setState(result.isSuccess)
        }
    }

    override suspend fun updatePassword(request: PasswordRequest) = tracer.trace("api.password") {
        origin.updatePassword(request).also { result ->
            it.setState(result.isSuccess)
        }
    }

    override suspend fun updateUser(request: CustomerDataRequest) =
        tracer.trace("api.user-update") {
            origin.updateUser(request).also { result ->
                it.setState(result.isSuccess)
            }
        }

    override suspend fun getPoints() = tracer.trace("api.points") {
        origin.getPoints().also { result ->
            it.setState(result.isSuccess)
        }
    }

    override suspend fun getUser() = tracer.trace("api.user") {
        origin.getUser().also { result ->
            it.setState(result.isSuccess)
        }
    }

    override suspend fun getBookings() = tracer.trace("api.bookings") {
        origin.getBookings().also { result ->
            it.setState(result.isSuccess)
        }
    }

    override suspend fun getBooking(id: String) = tracer.trace("api.booking") {
        origin.getBooking(id).also { result ->
            it.setState(result.isSuccess)
        }
    }

}