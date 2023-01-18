package movie.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import movie.core.EventCinemaFeature
import movie.core.EventFeature
import movie.core.EventFeatureDatabase
import movie.core.EventFeatureFilterUnseen
import movie.core.EventFeatureImpl
import movie.core.EventFeatureRating
import movie.core.EventFeatureRecover
import movie.core.EventFeatureRecoverSecondary
import movie.core.EventFeatureRequireNotEmpty
import movie.core.EventFeatureSort
import movie.core.EventFeatureSpotColor
import movie.core.EventFeatureStoring
import movie.core.EventShowingsFeature
import movie.core.EventShowingsFeatureCinemaCatch
import movie.core.EventShowingsFeatureCinemaDatabase
import movie.core.EventShowingsFeatureCinemaFold
import movie.core.EventShowingsFeatureCinemaNetwork
import movie.core.EventShowingsFeatureCinemaSort
import movie.core.EventShowingsFeatureCinemaStoring
import movie.core.EventShowingsFeatureCinemaUnseen
import movie.core.EventShowingsFeatureMovieCatch
import movie.core.EventShowingsFeatureMovieDatabase
import movie.core.EventShowingsFeatureMovieFold
import movie.core.EventShowingsFeatureMovieNetwork
import movie.core.EventShowingsFeatureMovieStoring
import movie.core.db.dao.BookingDao
import movie.core.db.dao.CinemaDao
import movie.core.db.dao.MovieDao
import movie.core.db.dao.MovieDetailDao
import movie.core.db.dao.MovieMediaDao
import movie.core.db.dao.MoviePreviewDao
import movie.core.db.dao.MovieRatingDao
import movie.core.db.dao.MovieReferenceDao
import movie.core.db.dao.ShowingDao
import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.model.Movie
import movie.core.nwk.CinemaService
import movie.core.nwk.EventService
import movie.core.preference.EventPreference
import movie.image.ImageAnalyzer
import movie.rating.LinkProvider
import movie.rating.RatingProvider
import movie.rating.di.Csfd
import movie.rating.di.Imdb
import movie.rating.di.RottenTomatoes

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
        saving: EventFeature,
        analyzer: ImageAnalyzer
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
        network = EventFeatureSpotColor(network, analyzer)
        network = EventFeatureSort(network)
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
        movieDao: MovieDao,
        ratingDao: MovieRatingDao,
        rating: RatingProvider,
        @RottenTomatoes
        tomatoes: LinkProvider,
        @Imdb
        imdb: LinkProvider,
        @Csfd
        csfd: LinkProvider
    ): EventFeature {
        var network: EventFeature
        network = EventFeatureImpl(event, cinema)
        network = EventFeatureStoring(
            network, cinemaDao, movieDao, detailDao, mediaDao,
            showingDao, referenceDao, previewDao
        )
        network = EventFeatureRating(network, ratingDao, rating, tomatoes, imdb, csfd)
        return network
    }

    @Provides
    fun showings(
        showing: ShowingDao,
        reference: MovieReferenceDao,
        service: EventService,
        preferences: EventPreference,
        booking: BookingDao,
        movie: MovieDao,
        cinema: EventCinemaFeature
    ): EventShowingsFeature.Factory = object : EventShowingsFeature.Factory {
        override fun cinema(cinema: Cinema): EventShowingsFeature.Cinema {
            var out: EventShowingsFeature.Cinema
            out = EventShowingsFeatureCinemaNetwork(service, cinema)
            out = EventShowingsFeatureCinemaStoring(out, movie, reference, showing)
            out = EventShowingsFeatureCinemaFold(
                // todo add invalidation of database data after 1D
                EventShowingsFeatureCinemaDatabase(showing, reference, cinema),
                out,
                // todo otherwise fallback to database as-is
            )
            out = EventShowingsFeatureCinemaUnseen(out, preferences, booking)
            out = EventShowingsFeatureCinemaSort(out)
            out = EventShowingsFeatureCinemaCatch(out)
            return out
        }

        override fun movie(movie: Movie, location: Location): EventShowingsFeature.Movie {
            var out: EventShowingsFeature.Movie
            out = EventShowingsFeatureMovieNetwork(movie, location, service, cinema)
            out = EventShowingsFeatureMovieStoring(out, movie, showing)
            out = EventShowingsFeatureMovieFold(
                // todo add invalidation of database data after 1D
                EventShowingsFeatureMovieDatabase(movie, location, showing, cinema),
                out,
                // todo otherwise fallback to database as-is
            )
            out = EventShowingsFeatureMovieCatch(out)
            return out
        }
    }

}

