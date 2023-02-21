package movie.wear

import com.google.android.gms.wearable.DataMap

interface WearService {
    suspend fun send(path: String, data: DataMap)
    suspend fun get(path: String): DataMap
    suspend fun remove(path: String)

    fun addListener(path: String, listener: OnChangedListener): OnChangedListener
    fun removeListener(listener: OnChangedListener)

    fun interface OnChangedListener {
        fun onChanged()
    }
}