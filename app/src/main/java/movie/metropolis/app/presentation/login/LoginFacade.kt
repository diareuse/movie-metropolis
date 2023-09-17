package movie.metropolis.app.presentation.login

import kotlinx.coroutines.flow.flow

interface LoginFacade {

    val currentUserEmail: String?
    val domain: String

    suspend fun getPosters(): List<String>

    suspend fun login(
        email: String,
        password: String
    ): Result<Unit>

    suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phone: String
    ): Result<Unit>

    companion object {

        fun LoginFacade.postersFlow() = flow {
            emit(getPosters())
        }

    }

}