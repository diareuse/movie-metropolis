package movie.core.db.dao

import androidx.room.Dao
import androidx.room.Query
import movie.core.db.model.MovieFavoriteStored

@Dao
interface MovieFavoriteDao : DaoBase<MovieFavoriteStored> {

    @Query("select * from movie_favorites")
    suspend fun selectAll(): List<MovieFavoriteStored>

    @Query("select * from movie_favorites")
    suspend fun selectPending(): List<MovieFavoriteStored>

    @Query("select * from movie_favorites as mf where mf.movie=:id")
    suspend fun select(id: String): MovieFavoriteStored?

    @Query("select exists(select 1 from movie_favorites where movie=:id)")
    suspend fun isFavorite(id: String): Boolean

    @Query("update movie_favorites set notified=1 where movie=:id")
    suspend fun setFavorite(id: String)

}