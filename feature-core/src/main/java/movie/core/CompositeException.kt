package movie.core

class CompositeException(
    vararg causes: Throwable
) : RuntimeException() {

    init {
        for (cause in causes)
            addSuppressed(cause)
    }

}