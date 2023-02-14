package movie.metropolis.app.presentation.order

import movie.core.UserCredentialFeature
import movie.metropolis.app.presentation.Listenable
import movie.metropolis.app.presentation.OnChangedListener

class OrderFacadeFromFeature(
    private val url: String,
    private val user: UserCredentialFeature
) : OrderFacade {

    private var currentUrl = ""
    private val listenable = Listenable<OnChangedListener>()

    override val isCompleted: Boolean
        get() = currentUrl.contains("OrderComplete", ignoreCase = true)

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

    override fun setUrl(url: String) {
        currentUrl = url
        listenable.notify { onChanged() }
    }

    override fun addOnChangedListener(listener: OnChangedListener): OnChangedListener {
        listenable += listener
        return listener
    }

    override fun removeOnChangedListener(listener: OnChangedListener) {
        listenable -= listener
    }

}