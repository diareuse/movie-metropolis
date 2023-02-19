package movie.core.util

@JvmName("requireListNotEmpty")
fun <T> Result<List<T>>.requireNotEmpty() = mapCatching {
    it.requireNotEmpty()
}

@JvmName("requireListNotEmpty")
fun <T> List<T>.requireNotEmpty() = apply {
    require(isNotEmpty()) { "List was empty" }
}

@JvmName("requireIterableNotEmpty")
fun <T> Result<Iterable<T>>.requireNotEmpty() = mapCatching {
    it.requireNotEmpty()
}

@JvmName("requireIterableNotEmpty")
fun <T> Iterable<T>.requireNotEmpty() = apply {
    @Suppress("ReplaceSizeZeroCheckWithIsEmpty")
    val isEmpty = when (this) {
        is List<*> -> isEmpty()
        else -> count() <= 0
    }
    require(!isEmpty) { "List was empty" }
}

@JvmName("requireMapNotEmpty")
fun <K, V> Result<Map<K, V>>.requireNotEmpty() = mapCatching {
    it.requireNotEmpty()
}

@JvmName("requireMapNotEmpty")
fun <K, V> Map<K, V>.requireNotEmpty() = apply {
    require(isNotEmpty()) { "Map was empty" }
}