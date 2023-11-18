package movie.core.db.dao

import movie.core.db.PerformanceTracer
import movie.core.db.model.MovieFavoriteStored
import movie.core.db.trace

class MovieFavoriteDaoPerformance(
    private val origin: MovieFavoriteDao,
    private val tracer: PerformanceTracer
) : MovieFavoriteDao {

    override suspend fun insert(model: MovieFavoriteStored) = tracer.trace("$Tag.insert") {
        origin.insert(model)
    }

    override suspend fun delete(model: MovieFavoriteStored) = tracer.trace("$Tag.delete") {
        origin.delete(model)
    }

    override suspend fun update(model: MovieFavoriteStored) = tracer.trace("$Tag.update") {
        origin.update(model)
    }

    override suspend fun selectAll() = tracer.trace("$Tag.select-all") {
        origin.selectAll()
    }

    override suspend fun isFavorite(id: String): Boolean = tracer.trace("$Tag.favorite") {
        origin.isFavorite(id)
    }

    override suspend fun selectPending() = tracer.trace("$Tag.selectPending") {
        origin.selectPending()
    }

    override suspend fun select(id: String) = tracer.trace("$Tag.select") {
        origin.select(id)
    }

    override suspend fun setFavorite(id: String) = tracer.trace("$Tag.setFavorite") {
        origin.setFavorite(id)
    }

    companion object {
        private const val Tag = "movie-favorite"
    }

}