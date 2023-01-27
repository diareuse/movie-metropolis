package movie.metropolis.app.presentation.order

import movie.core.UserCredentialFeature

class OrderFacadeFromFeature(
    private val url: String,
    private val user: UserCredentialFeature
) : OrderFacade {

    override suspend fun getRequest(): Result<RequestView> {
        val token = user.getToken().getOrNull()
        val headers = buildMap {
            if (token != null)
                put("access-token", token)
        }
        return RequestView(
            url = url,
            headers = headers
        ).let(Result.Companion::success)
    }

}