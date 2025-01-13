package movie.metropolis.app.presentation.order

import kotlinx.coroutines.flow.Flow

interface OrderFacade {

    val url: String
    val isCompleted: Flow<Boolean>

    suspend fun getHeaders(): Map<String, String>
    fun setUrl(url: String)

    fun interface Factory {
        fun create(url: String): OrderFacade
    }

}