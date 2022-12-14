package movie.core.preference

import movie.settings.ObservablePreference

interface EventPreference : ObservablePreference {

    var filterSeen: Boolean

}

