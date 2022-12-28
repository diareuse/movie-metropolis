package movie.image.database

import androidx.room.Dao
import androidx.room.Query

@Dao
internal interface AnalysisDao {

    @Query("select color from colors where image = :image and class = '${ColorStored.ClassDark}'")
    suspend fun getDark(image: String): Int?

    @Query("select color from colors where image = :image and class = '${ColorStored.ClassLight}'")
    suspend fun getLight(image: String): Int?

    @Query("select color from colors where image = :image and class = '${ColorStored.ClassVibrant}'")
    suspend fun getVibrant(image: String): Int?

}