package movie.wear

import com.google.android.gms.wearable.DataMap

class WearServiceCatching(
    private val origin: WearService
) : WearService by origin {

    override suspend fun send(path: String, data: DataMap) {
        origin
            .runCatching { send(path, data) }
            .onFailure { it.printStackTrace() }
    }

    override suspend fun get(path: String): DataMap {
        return origin
            .runCatching { get(path) }
            .onFailure { it.printStackTrace() }
            .getOrDefault(DataMap())
    }

    override suspend fun remove(path: String) {
        origin
            .runCatching { remove(path) }
            .onFailure { it.printStackTrace() }
    }

}