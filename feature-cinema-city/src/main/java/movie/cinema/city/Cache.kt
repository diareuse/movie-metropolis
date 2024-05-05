package movie.cinema.city

import androidx.collection.LruCache

internal inline fun <K : Any, V : Any> lruCache(
    maxSize: Int,
    crossinline sizeOf: (key: K, value: V) -> Int = { _, _ -> 1 },
    @Suppress("USELESS_CAST") // https://youtrack.jetbrains.com/issue/KT-21946
    crossinline create: (key: K) -> V? = { null as V? },
    crossinline onEntryRemoved: (evicted: Boolean, key: K, oldValue: V, newValue: V?) -> Unit =
        { _, _, _, _ -> }
): LruCache<K, V> {
    return object : LruCache<K, V>(maxSize) {
        override fun sizeOf(key: K, value: V) = sizeOf(key, value)
        override fun create(key: K) = create(key)
        override fun entryRemoved(evicted: Boolean, key: K, oldValue: V, newValue: V?) {
            onEntryRemoved(evicted, key, oldValue, newValue)
        }
    }
}