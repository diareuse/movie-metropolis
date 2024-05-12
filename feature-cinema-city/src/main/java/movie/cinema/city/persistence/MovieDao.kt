package movie.cinema.city.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
internal interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: MovieStored)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCast(items: List<MovieStored.Cast>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDirector(items: List<MovieStored.Director>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGenre(items: List<MovieStored.Genre>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertImage(items: List<MovieStored.Image>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertVideo(items: List<MovieStored.Video>)


    @Query("select * from movies as m where m.id=:id")
    suspend fun selectMovie(id: String): MovieStored

    @Query("select * from `movies-cast` as mc where mc.movie=:id")
    suspend fun selectCast(id: String): List<MovieStored.Cast>

    @Query("select * from `movies-director` as mc where mc.movie=:id")
    suspend fun selectDirector(id: String): List<MovieStored.Director>

    @Query("select * from `movies-genre` as mc where mc.movie=:id")
    suspend fun selectGenre(id: String): List<MovieStored.Genre>

    @Query("select * from `movies-image` as mc where mc.movie=:id")
    suspend fun selectImage(id: String): List<MovieStored.Image>

    @Query("select * from `movies-video` as mc where mc.movie=:id")
    suspend fun selectVideo(id: String): List<MovieStored.Video>
}