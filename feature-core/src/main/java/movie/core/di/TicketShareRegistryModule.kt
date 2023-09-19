package movie.core.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import movie.core.TicketShareRegistry
import movie.core.TicketShareRegistryCompress
import movie.core.TicketShareRegistryDeserialize
import movie.core.TicketShareRegistryEncode
import movie.core.TicketStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TicketShareRegistryModule {

    @Provides
    @Reusable
    fun registry(
        store: TicketStore = store(),
        json: Json = Json
    ): TicketShareRegistry {
        var registry: TicketShareRegistry
        registry = TicketShareRegistryDeserialize(store, json)
        registry = TicketShareRegistryCompress(registry)
        registry = TicketShareRegistryEncode(registry)
        return registry
    }

    @Provides
    @Singleton
    fun store(): TicketStore = TicketStore()

}