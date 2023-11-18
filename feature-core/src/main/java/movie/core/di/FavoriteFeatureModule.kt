package movie.core.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import movie.core.FavoriteFeature
import movie.core.FavoriteFeatureFromDatabase
import movie.core.FavoriteFeatureScheduleNotification
import movie.core.db.dao.MovieDao
import movie.core.db.dao.MovieFavoriteDao
import movie.pulse.ExactPulseScheduler

@Module
@InstallIn(SingletonComponent::class)
class FavoriteFeatureModule {

    @Provides
    @Reusable
    fun feature(
        favorite: MovieFavoriteDao,
        media: MovieDao,
        scheduler: ExactPulseScheduler
    ): FavoriteFeature {
        var feature: FavoriteFeature
        feature = FavoriteFeatureFromDatabase(favorite, media)
        feature = FavoriteFeatureScheduleNotification(feature, scheduler)
        return feature
    }

}