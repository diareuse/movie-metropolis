package movie.core.db.dao

import movie.core.db.model.MovieFavoriteStored

class MovieFavoriteDaoLowercase(
    private val origin: MovieFavoriteDao
) : MovieFavoriteDao by origin {

    override suspend fun insert(model: MovieFavoriteStored) {
        return origin.insert(model.lowercase())
    }

    override suspend fun delete(model: MovieFavoriteStored) {
        return origin.delete(model.lowercase())
    }

    override suspend fun update(model: MovieFavoriteStored) {
        return origin.update(model.lowercase())
    }

    override suspend fun isFavorite(id: String): Boolean {
        return origin.isFavorite(id.lowercase())
    }

    // ---

    private fun MovieFavoriteStored.lowercase() = copy(movie = movie.lowercase())

}