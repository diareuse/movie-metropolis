package movie.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import movie.core.FavoriteFeature
import movie.core.FavoriteFeatureFromDatabase
import movie.core.db.dao.MovieFavoriteDao
import movie.core.db.dao.MovieMediaDao

@Module
@InstallIn(SingletonComponent::class)
class FavoriteFeatureModule {

    @Provides
    fun feature(
        favorite: MovieFavoriteDao,
        media: MovieMediaDao
    ): FavoriteFeature {
        val feature: FavoriteFeature
        feature = FavoriteFeatureFromDatabase(favorite, media)
        return feature
    }

}