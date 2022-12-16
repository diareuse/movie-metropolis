package movie.core

interface FilterElement<T> {

    val options: List<T>
    fun apply()

}