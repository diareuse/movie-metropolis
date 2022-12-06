package movie.core

import movie.core.di.EventFeatureModule
import movie.core.nwk.di.NetworkModule

class EventFeatureTest : FeatureTest() {

    private lateinit var feature: EventFeature

    override fun prepare() {
        val module = EventFeatureModule()
        val service = NetworkModule()
        feature = module.feature(service.event(clientData), service.cinema(clientRoot))
    }

    // ---


}