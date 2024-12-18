package movie.metropolis.app.ui.setup

import androidx.compose.runtime.*
import movie.cinema.city.Region
import movie.metropolis.app.model.RegionView
import movie.metropolis.app.model.adapter.RegionViewFromFeature
import java.util.Locale

@Stable
class SetupViewState {
    val regions = mutableStateListOf<RegionView>(
        RegionViewFromFeature(Region.Czechia, Locale("cs", "CZ")),
        RegionViewFromFeature(Region.Slovakia, Locale("sk", "SK")),
        RegionViewFromFeature(Region.Poland, Locale("pl", "PL")),
        RegionViewFromFeature(Region.Hungary, Locale("hu", "HU")),
        RegionViewFromFeature(Region.Romania, Locale("ro", "RO"))
    )
    var region by mutableStateOf<RegionView?>(null)
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var loading by mutableStateOf(false)
    var error by mutableStateOf(false)
    val loginEnabled by derivedStateOf { email.isNotBlank() && password.isNotBlank() }
    val exitEnabled by derivedStateOf { region != null }
}