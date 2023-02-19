package movie.core

class MutableResult<T>(
    var value: Result<T> = Result.failure(IllegalStateException())
) {

    fun asResultCallback(): ResultCallback<T> = {
        value = it
    }

    companion object {

        inline operator fun <T> invoke(body: (MutableResult<T>) -> Unit): MutableResult<T> {
            return MutableResult<T>().apply(body)
        }

        inline fun <T> getOrNull(body: (MutableResult<T>) -> Unit) = invoke(body).value.getOrNull()

        inline fun <T> getOrThrow(body: (MutableResult<T>) -> Unit) =
            invoke(body).value.getOrThrow()

    }

}