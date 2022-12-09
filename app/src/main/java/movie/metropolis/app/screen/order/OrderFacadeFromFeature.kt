package movie.metropolis.app.screen.order

import android.util.Base64
import movie.core.UserFeature

class OrderFacadeFromFeature(
    encodedUrl: String,
    private val user: UserFeature
) : OrderFacade {

    private val url = Base64
        .decode(encodedUrl, Base64.NO_PADDING or Base64.URL_SAFE)
        .decodeToString()

    override suspend fun getRequest(): Result<RequestView> {
        val token = user.getToken().getOrThrow()
        return RequestView(
            url = url,
            headers = mapOf("access-token" to token)
        ).let(Result.Companion::success)
    }

}