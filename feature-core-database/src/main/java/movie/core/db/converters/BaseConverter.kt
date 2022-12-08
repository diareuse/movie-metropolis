package movie.core.db.converters

interface BaseConverter<From, To> {

    fun convertFrom(from: From): To
    fun convertTo(from: To): From

}