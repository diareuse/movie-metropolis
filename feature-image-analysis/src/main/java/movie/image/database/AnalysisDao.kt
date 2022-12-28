package movie.image.database

import androidx.room.Dao
import androidx.room.Query

@Dao
internal interface AnalysisDao {

    @Query("select color from colors where image = :image")
    suspend fun getColors(image: String): List<Int>

}