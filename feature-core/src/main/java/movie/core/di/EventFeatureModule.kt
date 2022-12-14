package movie.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import movie.core.EventFeature
import movie.core.EventFeatureDatabase
import movie.core.EventFeatureFilterUnseen
import movie.core.EventFeatureImpl
import movie.core.EventFeatureRecover
import movie.core.EventFeatureRecoverSecondary
import movie.core.EventFeatureRequireNotEmpty
import movie.core.EventFeatureStoring
import movie.core.db.dao.BookingDao
import movie.core.db.dao.CinemaDao
import movie.core.db.dao.MovieDao
import movie.core.db.dao.MovieDetailDao
import movie.core.db.dao.MovieMediaDao
import movie.core.db.dao.MoviePreviewDao
import movie.core.db.dao.MovieReferenceDao
import movie.core.db.dao.ShowingDao
import movie.core.nwk.CinemaService
import movie.core.nwk.EventService
import movie.core.preference.EventPreference

@Module
@InstallIn(SingletonComponent::class)
internal class EventFeatureModule {

    @Provides
    fun feature(
        showingDao: ShowingDao,
        cinemaDao: CinemaDao,
        detailDao: MovieDetailDao,
        mediaDao: MovieMediaDao,
        referenceDao: MovieReferenceDao,
        previewDao: MoviePreviewDao,
        bookingDao: BookingDao,
        preference: EventPreference,
        @Saving
        saving: EventFeature
    ): EventFeature {
        var database: EventFeature
        database = EventFeatureDatabase(
            showingDao, cinemaDao, detailDao,
            mediaDao, referenceDao, previewDao
        )
        database = EventFeatureRecover(database)
        database = EventFeatureRequireNotEmpty(database)
        var network: EventFeature = saving
        network = EventFeatureRecoverSecondary(database, network)
        network = EventFeatureFilterUnseen(network, preference, bookingDao)
        return network
    }

    @Saving
    @Provides
    fun featureSaving(
        event: EventService,
        cinema: CinemaService,
        showingDao: ShowingDao,
        cinemaDao: CinemaDao,
        detailDao: MovieDetailDao,
        mediaDao: MovieMediaDao,
        referenceDao: MovieReferenceDao,
        previewDao: MoviePreviewDao,
        movieDao: MovieDao
    ): EventFeature {
        var network: EventFeature
        network = EventFeatureImpl(event, cinema)
        network = EventFeatureStoring(
            network, cinemaDao, movieDao, detailDao, mediaDao,
            showingDao, referenceDao, previewDao
        )
        return network
    }

}

