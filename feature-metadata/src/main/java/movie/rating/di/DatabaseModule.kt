package movie.rating.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import movie.rating.database.RatingDao
import movie.rating.database.RatingDatabase

@InstallIn(SingletonComponent::class)
@Module
internal class DatabaseModule {

    @Provides
    fun database(
        @ApplicationContext
        context: Context
    ): RatingDatabase {
        val name = context.packageName + ".rating"
        return Room.databaseBuilder(context, RatingDatabase::class.java, name)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun dao(database: RatingDatabase): RatingDao = database.rating()

}