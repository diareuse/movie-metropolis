package movie.core

import movie.core.model.SignInMethod

interface UserCredentialFeature {
    val email: String?
    suspend fun signIn(method: SignInMethod): Result<Unit>
    suspend fun getToken(): Result<String>
}