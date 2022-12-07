package movie.core.nwk

interface Cache<Key : Any, Value : Any> {

    suspend fun get(key: Key): Value?
    suspend fun put(key: Key, value: Value?)
    suspend fun clear()

}


