package movie.core.db.dao

import androidx.room.Dao
import androidx.room.Query
import movie.core.db.model.MovieFavoriteStored
import movie.core.db.model.MoviePreviewView

@Dao
interface MovieFavoriteDao : DaoBase<MovieFavoriteStored> {

    @Query("select * from movie_preview_views where exists(select 1 from movie_favorites where movie_favorites.movie=movie_preview_views.id) order by (select created_at from movie_favorites where movie_favorites.movie=movie_preview_views.id) desc")
    suspend fun selectAll(): List<MoviePreviewView>

    @Query("select exists(select 1 from movie_favorites where movie=:id)")
    suspend fun isFavorite(id: String): Boolean

}