package movie.metropolis.app.screen

typealias LoadableList<Type> = Loadable<List<Type>>

sealed class Loadable<Type> {

    class Loading<Type>(val initial: Type?) : Loadable<Type>()
    class Loaded<Type>(val result: Type) : Loadable<Type>()
    class Error<Type>(val error: Throwable) : Loadable<Type>()

    companion object {

        fun <T> loadingOf(initial: T? = null) = Loading(initial)
        fun <T> loadedOf(result: Result<T>) = result.fold({ Loaded(it) }, { Error(it) })

    }

}