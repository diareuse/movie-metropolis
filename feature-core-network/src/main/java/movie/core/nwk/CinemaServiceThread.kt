package movie.core.nwk

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CinemaServiceThread(
    private val origin: CinemaService
) : CinemaService {

    override suspend fun getCinemas() = withContext(Dispatchers.IO) {
        origin.getCinemas()
    }

    override suspend fun getPromoCards() = withContext(Dispatchers.IO) {
        origin.getPromoCards()
    }
}