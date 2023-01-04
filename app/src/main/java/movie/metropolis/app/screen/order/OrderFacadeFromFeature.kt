package movie.metropolis.app.screen.order

import movie.core.UserFeature

class OrderFacadeFromFeature(
    private val url: String,
    private val user: UserFeature
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