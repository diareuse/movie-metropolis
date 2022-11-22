package movie.metropolis.app.screen

typealias LoadableList<Type> = Loadable<List<Type>>

sealed class Loadable<Type> {

    data class Loading<Type>(val initial: Type?) : Loadable<Type>()
    data class Loaded<Type>(val result: Type) : Loadable<Type>()
    data class Error<Type>(val error: Throwable) : Loadable<Type>()

    companion object {

        fun <T> loadingOf(initial: T? = null) = Loading(initial)
        fun <T> loadedOf(result: Result<T>) = result.fold({ Loaded(it) }, { Error(it) })

    }

}