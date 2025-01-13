package movie.metropolis.app.presentation.order

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import movie.cinema.city.CinemaCity

class OrderFacadeFromFeature(
    override val url: String,
    private val cinemaCity: CinemaCity
) : OrderFacade {

    private val currentUrl = MutableStateFlow("")

    override val isCompleted = currentUrl.map { it.contains("OrderComplete", ignoreCase = true) }

    override suspend fun getHeaders() = buildMap {
        put("access-token", cinemaCity.customers.getToken())
    }

    override fun setUrl(url: String) {
        currentUrl.value = url
    }

}