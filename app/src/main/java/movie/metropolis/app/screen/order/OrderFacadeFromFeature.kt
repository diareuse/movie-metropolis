package movie.metropolis.app.screen.order

import movie.core.UserFeature

class OrderFacadeFromFeature(
    private val url: String,
    private val user: UserFeature
) : OrderFacade {

    override suspend fun getRequest(): Result<RequestView> {
        val token = user.getToken().getOrThrow()
        return RequestView(
            url = url,
            headers = mapOf("access-token" to token)
        ).let(Result.Companion::success)
    }

}