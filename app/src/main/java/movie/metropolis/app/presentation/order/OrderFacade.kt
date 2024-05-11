package movie.metropolis.app.presentation.order

import kotlinx.coroutines.flow.Flow

interface OrderFacade {

    val isCompleted: Flow<Boolean>

    suspend fun getRequest(): Result<RequestView>
    fun setUrl(url: String)

    fun interface Factory {
        fun create(url: String): OrderFacade
    }

}