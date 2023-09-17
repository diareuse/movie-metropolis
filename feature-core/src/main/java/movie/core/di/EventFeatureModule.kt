package movie.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import movie.core.EventCinemaFeature
import movie.core.EventCinemaFeatureCatch
import movie.core.EventCinemaFeatureDatabase
import movie.core.EventCinemaFeatureDistance
import movie.core.EventCinemaFeatureDistanceClosest
import movie.core.EventCinemaFeatureFold
import movie.core.EventCinemaFeatureInvalidateAfter
import movie.core.EventCinemaFeatureNetwork
import movie.core.EventCinemaFeatureRequireNotEmpty
import movie.core.EventCinemaFeatureSaveTimestamp
import movie.core.EventCinemaFeatureSort
import movie.core.EventCinemaFeatureStoring
import movie.core.EventDetailFeature
import movie.core.EventDetailFeatureCatch
import movie.core.EventDetailFeatureDatabase
import movie.core.EventDetailFeatureFold
import movie.core.EventDetailFeatureNetwork
import movie.core.EventDetailFeatureStoring
import movie.core.EventPreviewFeature
import movie.core.EventPreviewFeatureCatch
import movie.core.EventPreviewFeatureDatabase
import movie.core.EventPreviewFeatureFilter
import movie.core.EventPreviewFeatureFilterMovie
import movie.core.EventPreviewFeatureFold
import movie.core.EventPreviewFeatureInvalidateAfter
import movie.core.EventPreviewFeatureNetwork
import movie.core.EventPreviewFeatureRequireNotEmpty
import movie.core.EventPreviewFeatureSaveTimestamp
import movie.core.EventPreviewFeatureSort
import movie.core.EventPreviewFeatureStoring
import movie.core.EventPromoFeature
import movie.core.EventPromoFeatureCatch
import movie.core.EventPromoFeatureDatabase
import movie.core.EventPromoFeatureFold
import movie.core.EventPromoFeatureNetworkAndUpdate
import movie.core.EventPromoFeatureUrlWrap
import movie.core.EventShowingsFeature
import movie.core.EventShowingsFeatureCinemaCatch
import movie.core.EventShowingsFeatureCinemaDatabase
import movie.core.EventShowingsFeatureCinemaFold
import movie.core.EventShowingsFeatureCinemaNetwork
import movie.core.EventShowingsFeatureCinemaRequireNotEmpty
import movie.core.EventShowingsFeatureCinemaSort
import movie.core.EventShowingsFeatureCinemaStoring
import movie.core.EventShowingsFeatureCinemaTimeout
import movie.core.EventShowingsFeatureCinemaUnseen
import movie.core.EventShowingsFeatureMovieCatch
import movie.core.EventShowingsFeatureMovieDatabase
import movie.core.EventShowingsFeatureMovieFold
import movie.core.EventShowingsFeatureMovieNetwork
import movie.core.EventShowingsFeatureMovieRequireNotEmpty
import movie.core.EventShowingsFeatureMovieStoring
import movie.core.EventShowingsFeatureMovieTimeout
import movie.core.db.dao.BookingDao
import movie.core.db.dao.CinemaDao
import movie.core.db.dao.MovieDao
import movie.core.db.dao.MovieDetailDao
import movie.core.db.dao.MovieMediaDao
import movie.core.db.dao.MoviePreviewDao
import movie.core.db.dao.MoviePromoDao
import movie.core.db.dao.MovieReferenceDao
import movie.core.db.dao.ShowingDao
import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.model.Movie
import movie.core.nwk.CinemaService
import movie.core.nwk.EndpointProvider
import movie.core.nwk.EventService
import movie.core.nwk.model.ShowingType
import movie.core.preference.EventPreference
import movie.core.preference.SyncPreference
import kotlin.time.Duration.Companion.days

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
        cinema: EventCinemaFeature,
        scope: CoroutineScope
    ): EventShowingsFeature.Factory = object : EventShowingsFeature.Factory {
        override fun cinema(cinema: Cinema): EventShowingsFeature.Cinema {
            val database = EventShowingsFeatureCinemaDatabase(showing, reference, cinema)
            var out: EventShowingsFeature.Cinema
            out = EventShowingsFeatureCinemaNetwork(service, cinema)
            out = EventShowingsFeatureCinemaStoring(out, movie, reference, showing, scope)
            out = EventShowingsFeatureCinemaFold(
                EventShowingsFeatureCinemaTimeout(out),
                EventShowingsFeatureCinemaRequireNotEmpty(database),
                out,
                database
            )
            out = EventShowingsFeatureCinemaUnseen(out, preferences, booking)
            out = EventShowingsFeatureCinemaSort(out)
            out = EventShowingsFeatureCinemaCatch(out)
            return out
        }

        override fun movie(movie: Movie, location: Location): EventShowingsFeature.Movie {
            val database = EventShowingsFeatureMovieDatabase(movie, location, showing, cinema)
            var out: EventShowingsFeature.Movie
            out = EventShowingsFeatureMovieNetwork(movie, location, service, cinema)
            out = EventShowingsFeatureMovieStoring(out, movie, showing, scope)
            out = EventShowingsFeatureMovieFold(
                EventShowingsFeatureMovieTimeout(out),
                EventShowingsFeatureMovieRequireNotEmpty(database),
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
        preference: EventPreference,
        booking: BookingDao,
        sync: SyncPreference,
        scope: CoroutineScope
    ): EventPreviewFeature.Factory = object : EventPreviewFeature.Factory {

        override fun current(): EventPreviewFeature = common(ShowingType.Current)

        override fun upcoming(): EventPreviewFeature = common(ShowingType.Upcoming)

        private fun common(type: ShowingType): EventPreviewFeature {
            val fallback = EventPreviewFeatureDatabase(preview, media, type)
            var db: EventPreviewFeature
            db = EventPreviewFeatureDatabase(preview, media, type)
            db = EventPreviewFeatureRequireNotEmpty(db)
            db = EventPreviewFeatureInvalidateAfter(db, sync, type, 1.days)
            var out: EventPreviewFeature
            out = EventPreviewFeatureNetwork(service, type)
            out = EventPreviewFeatureStoring(out, type, movie, preview, media, scope)
            out = EventPreviewFeatureSaveTimestamp(out, sync, type)
            out = EventPreviewFeatureFold(db, out, fallback)
            out = EventPreviewFeatureFilterMovie(out, preference)
            out = EventPreviewFeatureSort(out)
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
        scope: CoroutineScope
    ): EventDetailFeature {
        var out: EventDetailFeature
        out = EventDetailFeatureNetwork(service)
        out = EventDetailFeatureStoring(out, movie, detail, media, scope)
        out = EventDetailFeatureFold(EventDetailFeatureDatabase(detail, media), out)
        out = EventDetailFeatureCatch(out)
        return out
    }

    @Provides
    fun cinema(
        service: CinemaService,
        cinema: CinemaDao,
        preference: EventPreference,
        sync: SyncPreference,
        provider: EndpointProvider,
        scope: CoroutineScope
    ): EventCinemaFeature {
        val fallback = EventCinemaFeatureDatabase(cinema)
        var db: EventCinemaFeature
        db = EventCinemaFeatureDatabase(cinema)
        db = EventCinemaFeatureRequireNotEmpty(db)
        db = EventCinemaFeatureInvalidateAfter(db, sync, 30.days)
        var out: EventCinemaFeature
        out = EventCinemaFeatureNetwork(service, provider)
        out = EventCinemaFeatureStoring(out, cinema, scope)
        out = EventCinemaFeatureSaveTimestamp(out, sync)
        out = EventCinemaFeatureFold(db, out, fallback)
        out = EventCinemaFeatureDistance(out)
        out = EventCinemaFeatureDistanceClosest(out, preference)
        out = EventCinemaFeatureSort(out)
        out = EventCinemaFeatureCatch(out)
        return out
    }

    @Provides
    fun promo(
        dao: MoviePromoDao,
        service: CinemaService,
        provider: EndpointProvider,
        scope: CoroutineScope
    ): EventPromoFeature {
        var out: EventPromoFeature
        out = EventPromoFeatureFold(
            EventPromoFeatureDatabase(dao),
            EventPromoFeatureNetworkAndUpdate(service, dao, scope)
        )
        out = EventPromoFeatureUrlWrap(out, provider)
        out = EventPromoFeatureCatch(out)
        return out
    }

}

