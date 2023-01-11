package movie.core

import movie.core.model.Region

interface SetupFeature {

    val requiresSetup: Boolean
    var region: Region

}