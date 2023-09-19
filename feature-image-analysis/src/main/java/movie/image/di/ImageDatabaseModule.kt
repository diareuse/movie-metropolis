package movie.image.di

import android.content.Context
import androidx.annotation.OptIn
import androidx.room.ExperimentalRoomApi
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import movie.image.database.AnalysisDao
import movie.image.database.ColorDao
import movie.image.database.ImageDao
import movie.image.database.ImageDatabase

@Module
@InstallIn(SingletonComponent::class)
internal class ImageDatabaseModule {

    @OptIn(ExperimentalRoomApi::class)
    @Provides
    @Reusable
    fun database(
        @ApplicationContext
        context: Context
    ): ImageDatabase {
        val name = context.packageName + ".image.analysis"
        return Room.databaseBuilder(context, ImageDatabase::class.java, name)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Reusable
    fun analysis(
        database: ImageDatabase
    ): AnalysisDao {
        return database.analysis()
    }

    @Provides
    @Reusable
    fun image(
        database: ImageDatabase
    ): ImageDao {
        return database.image()
    }

    @Provides
    @Reusable
    fun color(
        database: ImageDatabase
    ): ColorDao {
        return database.color()
    }

}