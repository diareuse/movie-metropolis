package movie.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import movie.core.SetupFeature
import movie.core.SetupFeatureImpl
import movie.core.nwk.EndpointProvider
import movie.core.preference.RegionPreference

@Module
@InstallIn(SingletonComponent::class)
class SetupFeatureModule {

    @Provides
    fun feature(
        preference: RegionPreference
    ): SetupFeature {
        return SetupFeatureImpl(preference)
    }

    @Provides
    fun endpoint(
        feature: SetupFeature
    ): EndpointProvider = object : EndpointProvider {
        override val domain: String
            get() = feature.region.domain
        override val id: Int
            get() = feature.region.id
    }

}