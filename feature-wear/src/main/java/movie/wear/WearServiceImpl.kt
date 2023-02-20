package movie.wear

import androidx.core.net.toUri
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataMap
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.PutDataMapRequest
import kotlinx.coroutines.tasks.await

class WearServiceImpl(
    private val client: DataClient
) : WearService {

    override suspend fun send(path: String, data: DataMap) {
        val request = PutDataMapRequest.create(path)
            .apply { dataMap.putAll(data) }
            .asPutDataRequest()
            .setUrgent()
        client.putDataItem(request).await()
    }

    override suspend fun get(path: String): DataMap {
        return client.getDataItems(path.toUri()).await().first()
            .let(DataMapItem::fromDataItem)
            .dataMap
    }

    override suspend fun remove(path: String) {
        client.deleteDataItems(path.toUri()).await()
    }

}