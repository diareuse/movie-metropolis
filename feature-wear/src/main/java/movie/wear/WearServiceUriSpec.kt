package movie.wear

import com.google.android.gms.wearable.DataMap

class WearServiceUriSpec(
    private val origin: WearService
) : WearService by origin {

    override suspend fun get(path: String): DataMap {
        return origin.get("wear://*$path")
    }

    override suspend fun remove(path: String) {
        return origin.remove("wear://*$path")
    }

}