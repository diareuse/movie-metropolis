package movie.wear

import android.os.Bundle
import androidx.core.net.toUri
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEventBuffer
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
        val items = client.getDataItems(path.toUri()).await()
        val item = items.firstOrNull() ?: return DataMap.fromBundle(Bundle.EMPTY)
        return item
            .let(DataMapItem::fromDataItem)
            .dataMap
    }

    override suspend fun remove(path: String) {
        client.deleteDataItems(path.toUri()).await()
    }

    override fun addListener(
        path: String,
        listener: WearService.OnChangedListener
    ): WearService.OnChangedListener {
        val combined = CombinedListener(listener)
        client.addListener(combined, path.toUri(), DataClient.FILTER_LITERAL)
        return combined
    }

    override fun removeListener(listener: WearService.OnChangedListener) {
        require(listener is CombinedListener)
        client.removeListener(listener)
    }

    private class CombinedListener(
        listener: WearService.OnChangedListener
    ) : WearService.OnChangedListener by listener, DataClient.OnDataChangedListener {
        override fun onDataChanged(p0: DataEventBuffer) = onChanged()
    }

}