package movie.calendar.di

import android.content.ContentResolver
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import movie.calendar.CalendarList
import movie.calendar.CalendarListPlatform
import movie.calendar.CalendarListRecover
import movie.calendar.CalendarWriter
import movie.calendar.CalendarWriterPlatform
import movie.calendar.CalendarWriterPreventIfExists

@Module
@InstallIn(SingletonComponent::class)
internal class CalendarModule {

    @Provides
    fun list(
        resolver: ContentResolver
    ): CalendarList {
        var out: CalendarList
        out = CalendarListPlatform(resolver)
        out = CalendarListRecover(out)
        return out
    }

    @Provides
    fun writer(
        resolver: ContentResolver
    ): CalendarWriter.Factory = CalendarWriter.Factory { id ->
        var writer: CalendarWriter
        writer = CalendarWriterPlatform(resolver, id)
        writer = CalendarWriterPreventIfExists(writer, resolver)
        writer
    }

    @Provides
    fun resolver(
        @ApplicationContext
        context: Context
    ): ContentResolver {
        return context.contentResolver
    }

}