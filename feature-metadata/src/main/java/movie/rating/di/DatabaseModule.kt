package movie.rating.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import movie.rating.database.ActorDao
import movie.rating.database.ActorReferenceConnectionDao
import movie.rating.database.ActorReferenceDao
import movie.rating.database.MetadataDatabase
import movie.rating.database.RatingDao
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal class DatabaseModule {

    @Provides
    @Singleton
    fun database(
        @ApplicationContext
        context: Context
    ): MetadataDatabase {
        val name = context.packageName + ".rating"
        return Room.databaseBuilder(context, MetadataDatabase::class.java, name)
            .setAutoCloseTimeout(30, TimeUnit.SECONDS)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Reusable
    fun rating(database: MetadataDatabase): RatingDao =
        database.rating()

    @Provides
    @Reusable
    fun actor(database: MetadataDatabase): ActorDao =
        database.actor()

    @Provides
    @Reusable
    fun actorReferenceConnection(database: MetadataDatabase): ActorReferenceConnectionDao =
        database.actorReferenceConnection()

    @Provides
    @Reusable
    fun actorReference(database: MetadataDatabase): ActorReferenceDao =
        database.actorReference()

}