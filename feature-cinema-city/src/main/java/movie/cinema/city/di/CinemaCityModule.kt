package movie.cinema.city.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import movie.cinema.city.CinemaCity
import movie.cinema.city.CinemaCityAuth
import movie.cinema.city.CinemaCityClient
import movie.cinema.city.CinemaCityClientCaching
import movie.cinema.city.CinemaCityClientImpl
import movie.cinema.city.CinemaCityStorage
import movie.cinema.city.CredentialsProvider
import movie.cinema.city.RegionProvider
import movie.cinema.city.TokenStore
import movie.cinema.city.persistence.MovieStorage
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CinemaCityModule {

    @Provides
    @Singleton
    internal fun cinemaCity(
        endpoint: RegionProvider,
        credentials: CredentialsProvider,
        token: TokenStore,
        database: MovieStorage
    ): CinemaCity {
        var client: CinemaCityClient
        client = CinemaCityClientImpl(CinemaCityAuth, endpoint, credentials, token)
        client = CinemaCityClientCaching(client)
        return CinemaCityStorage(client, database.movie())
    }

    @Provides
    @Singleton
    internal fun database(
        @ApplicationContext
        context: Context
    ): MovieStorage {
        val name = context.packageName + ".movies"
        return Room.databaseBuilder(context, MovieStorage::class.java, name)
            .fallbackToDestructiveMigration()
            .setAutoCloseTimeout(1, TimeUnit.SECONDS)
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .setQueryExecutor(Dispatchers.IO.asExecutor())
            .setTransactionExecutor(Dispatchers.IO.asExecutor())
            .build()
    }

}