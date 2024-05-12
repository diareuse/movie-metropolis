package movie.cinema.city.persistence

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import movie.cinema.city.Cinema
import java.net.URL

@TypeConverters(URLConverter::class, DateConverter::class)
@Database(
    entities = [
        MovieStored::class,
        MovieStored.Cast::class,
        MovieStored.Director::class,
        MovieStored.Genre::class,
        MovieStored.Video::class,
        MovieStored.Image::class,
        TicketStored::class,
        TicketStored.Reservation::class,
        CinemaStored::class
    ],
    version = 1
)
internal abstract class MovieStorage : RoomDatabase() {
    abstract fun movie(): MovieDao
    abstract fun tickets(): TicketDao
    abstract fun cinemas(): CinemaDao
}

@Dao
internal interface CinemaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(items: List<CinemaStored>)

    @Query("select * from cinemas")
    suspend fun select(): List<CinemaStored>
}

@TypeConverters(AddressConverter::class)
@Entity(tableName = "cinemas")
internal data class CinemaStored(
    @PrimaryKey
    @ColumnInfo("id") val id: String,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("image") val image: URL,
    @ColumnInfo("description") val description: String,
    @ColumnInfo("postalCode") val postalCode: String,
    @ColumnInfo("address") val address: List<String>,
    @ColumnInfo("latitude") val latitude: Double,
    @ColumnInfo("longitude") val longitude: Double,
    @ColumnInfo("city") val city: String,
) {
    constructor(cinema: Cinema) : this(
        id = cinema.id,
        name = cinema.name,
        image = cinema.image,
        description = cinema.description,
        postalCode = cinema.address.postalCode,
        address = cinema.address.address,
        latitude = cinema.address.latitude,
        longitude = cinema.address.longitude,
        city = cinema.address.city,
    )
}

internal data class CinemaFromDatabase(
    private val stored: CinemaStored
) : Cinema {
    override val id: String
        get() = stored.id
    override val name: String
        get() = stored.name
    override val image: URL
        get() = stored.image
    override val description: String
        get() = stored.description
    override val address: Cinema.Address = Address()

    private inner class Address : Cinema.Address {
        override val postalCode: String
            get() = stored.postalCode
        override val address: List<String>
            get() = stored.address
        override val latitude: Double
            get() = stored.latitude
        override val longitude: Double
            get() = stored.longitude
        override val city: String
            get() = stored.city
    }
}