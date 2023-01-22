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
    @Suppress("ReplaceSizeCheckWithIsNotEmpty")
    require(count() > 0) { "List was empty" }
}

@JvmName("requireMapNotEmpty")
fun <K, V> Result<Map<K, V>>.requireNotEmpty() = mapCatching {
    it.requireNotEmpty()
}

@JvmName("requireMapNotEmpty")
fun <K, V> Map<K, V>.requireNotEmpty() = apply {
    require(isNotEmpty()) { "Map was empty" }
}