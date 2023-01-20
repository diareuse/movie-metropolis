package movie.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import movie.core.EventCinemaFeature
import movie.core.EventCinemaFeatureCatch
import movie.core.EventCinemaFeatureDatabase
import movie.core.EventCinemaFeatureDistance
import movie.core.EventCinemaFeatureDistanceClosest
import movie.core.EventCinemaFeatureFold
import movie.core.EventCinemaFeatureNetwork
import movie.core.EventCinemaFeatureRequireNotEmpty
import movie.core.EventCinemaFeatureSort
import movie.core.EventCinemaFeatureStoring
import movie.core.EventDetailFeature
import movie.core.EventDetailFeatureCatch
import movie.core.EventDetailFeatureDatabase
import movie.core.EventDetailFeatureFold
import movie.core.EventDetailFeatureNetwork
import movie.core.EventDetailFeatureNetworkRating
import movie.core.EventDetailFeatureSpotColor
import movie.core.EventDetailFeatureStoring
import movie.core.EventPreviewFeature
import movie.core.EventPreviewFeatureCatch
import movie.core.EventPreviewFeatureDatabase
import movie.core.EventPreviewFeatureFilter
import movie.core.EventPreviewFeatureFold
import movie.core.EventPreviewFeatureNetwork
import movie.core.EventPreviewFeatureRequireNotEmpty
import movie.core.EventPreviewFeatureSort
import movie.core.EventPreviewFeatureSpotColor
import movie.core.EventPreviewFeatureStoring
import movie.core.EventShowingsFeature
import movie.core.EventShowingsFeatureCinemaCatch
import movie.core.EventShowingsFeatureCinemaDatabase
import movie.core.EventShowingsFeatureCinemaFold
import movie.core.EventShowingsFeatureCinemaNetwork
import movie.core.EventShowingsFeatureCinemaRequireNotEmpty
import movie.core.EventShowingsFeatureCinemaSort
import movie.core.EventShowingsFeatureCinemaStoring
import movie.core.EventShowingsFeatureCinemaUnseen
import movie.core.EventShowingsFeatureMovieCatch
import movie.core.EventShowingsFeatureMovieDatabase
import movie.core.EventShowingsFeatureMovieFold
import movie.core.EventShowingsFeatureMovieNetwork
import movie.core.EventShowingsFeatureMovieRequireNotEmpty
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
import movie.core.nwk.model.ShowingType
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
                EventShowingsFeatureCinemaRequireNotEmpty(
                    EventShowingsFeatureCinemaDatabase(showing, reference, cinema)
                ),
                out
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
                EventShowingsFeatureMovieRequireNotEmpty(
                    EventShowingsFeatureMovieDatabase(movie, location, showing, cinema)
                ),
                out
            )
            out = EventShowingsFeatureMovieCatch(out)
            return out
        }
    }

    @Provides
    fun preview(
        service: EventService,
        movie: MovieDao,
        preview: MoviePreviewDao,
        media: MovieMediaDao,
        analyzer: ImageAnalyzer,
        preference: EventPreference,
        booking: BookingDao
    ): EventPreviewFeature.Factory = object : EventPreviewFeature.Factory {

        override fun current(): EventPreviewFeature = common(ShowingType.Current)

        override fun upcoming(): EventPreviewFeature = common(ShowingType.Upcoming)

        private fun common(type: ShowingType): EventPreviewFeature {
            var out: EventPreviewFeature
            out = EventPreviewFeatureNetwork(service, type)
            out = EventPreviewFeatureStoring(out, type, movie, preview, media)
            out = EventPreviewFeatureFold(
                // todo add invalidation of database data after 1D
                EventPreviewFeatureRequireNotEmpty(
                    EventPreviewFeatureDatabase(preview, media, type)
                ),
                out
                // todo otherwise fallback to database as-is
            )
            out = EventPreviewFeatureSort(out)
            out = EventPreviewFeatureSpotColor(out, analyzer)
            out = EventPreviewFeatureFilter(out, preference, booking)
            out = EventPreviewFeatureCatch(out)
            return out
        }
    }

    @Provides
    fun detail(
        service: EventService,
        movie: MovieDao,
        detail: MovieDetailDao,
        media: MovieMediaDao,
        ratings: MovieRatingDao,
        rating: RatingProvider,
        @RottenTomatoes tomatoes: LinkProvider,
        @Imdb imdb: LinkProvider,
        @Csfd csfd: LinkProvider,
        analyzer: ImageAnalyzer
    ): EventDetailFeature {
        var out: EventDetailFeature
        out = EventDetailFeatureNetwork(service)
        out = EventDetailFeatureStoring(out, movie, detail, media)
        out = EventDetailFeatureNetworkRating(out, ratings, rating, tomatoes, imdb, csfd)
        out = EventDetailFeatureFold(
            EventDetailFeatureDatabase(detail, media),
            out
        )
        out = EventDetailFeatureSpotColor(out, analyzer)
        out = EventDetailFeatureCatch(out)
        return out
    }

    @Provides
    fun cinema(
        service: CinemaService,
        cinema: CinemaDao,
        preference: EventPreference
    ): EventCinemaFeature {
        var out: EventCinemaFeature
        out = EventCinemaFeatureNetwork(service)
        out = EventCinemaFeatureStoring(out, cinema)
        out = EventCinemaFeatureFold(
            // todo add invalidation of database data after 1M
            EventCinemaFeatureRequireNotEmpty(
                EventCinemaFeatureDatabase(cinema)
            ),
            out
            // todo otherwise fallback to database as-is
        )
        out = EventCinemaFeatureDistance(out)
        out = EventCinemaFeatureDistanceClosest(out, preference)
        out = EventCinemaFeatureSort(out)
        out = EventCinemaFeatureCatch(out)
        return out
    }

}

