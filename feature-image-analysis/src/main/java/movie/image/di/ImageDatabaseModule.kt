package movie.image.di

import android.content.Context
import androidx.annotation.OptIn
import androidx.room.ExperimentalRoomApi
import androidx.room.Room
import dagger.Module
import dagger.Provides
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
    fun database(
        @ApplicationContext
        context: Context
    ): ImageDatabase {
        val name = context.packageName + ".image.analysis"
        return Room.databaseBuilder(context, ImageDatabase::class.java, name)
            //.setAutoCloseTimeout(1, TimeUnit.SECONDS)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun analysis(
        database: ImageDatabase
    ): AnalysisDao {
        return database.analysis()
    }

    @Provides
    fun image(
        database: ImageDatabase
    ): ImageDao {
        return database.image()
    }

    @Provides
    fun color(
        database: ImageDatabase
    ): ColorDao {
        return database.color()
    }

}