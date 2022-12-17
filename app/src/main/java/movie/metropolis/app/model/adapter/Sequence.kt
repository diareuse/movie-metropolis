package movie.metropolis.app.model.adapter

fun <T> Sequence<T>.middleOrNull(): T? {
    val output = toList().also { println(it) }
    if (output.isEmpty()) return null
    return output[output.size / 2]
}