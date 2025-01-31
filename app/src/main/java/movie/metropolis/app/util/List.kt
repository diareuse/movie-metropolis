package movie.metropolis.app.util

fun <T> MutableList<T>.updateWith(items: List<T>) {
    val remove = mutableListOf<T>()
    val add = mutableListOf<T>()
    for (index in 0..<maxOf(size, items.size)) {
        val mine = getOrNull(index)
        val other = items.getOrNull(index)
        if (mine != null && mine !in items) remove.add(mine)
        if (other != null && other !in this) add.add(other)
    }

    removeAll(remove)
    addAll(add)

    remove.clear()
    add.clear()

    sortBy { items.indexOf(it) }
}