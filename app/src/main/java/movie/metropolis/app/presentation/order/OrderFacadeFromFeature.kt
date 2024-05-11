package movie.metropolis.app.presentation.order

import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import movie.cinema.city.CinemaCity

class OrderFacadeFromFeature(
    private val url: String,
    private val cinemaCity: CinemaCity
) : OrderFacade {

    private val currentUrl = MutableStateFlow("")

    override val isCompleted = currentUrl.map { it.contains("OrderComplete", ignoreCase = true) }

    override suspend fun getRequest(): Result<RequestView> {
        val headers = buildMap {
            put("access-token", cinemaCity.customers.getToken())
        }
        return RequestView(
            url = url,
            headers = headers.toImmutableMap()
        ).let(Result.Companion::success)
    }

    override fun setUrl(url: String) {
        currentUrl.value = url
    }

}