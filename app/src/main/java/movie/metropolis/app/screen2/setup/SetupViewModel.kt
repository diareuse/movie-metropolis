package movie.metropolis.app.screen2.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import movie.metropolis.app.model.RegionView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.setup.SetupFacade
import movie.metropolis.app.presentation.setup.SetupFacade.Companion.regionsFlow
import movie.metropolis.app.presentation.setup.SetupFacade.Companion.requiresSetupFlow
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val facade: SetupFacade
) : ViewModel() {

    val requiresSetup = facade.requiresSetupFlow
    val regions = facade.regionsFlow
        .retainStateIn(viewModelScope, Loadable.loading())

    val posters = listOf(
        "https://www.themoviedb.org/t/p/w1280/lPsD10PP4rgUGiGR4CCXA6iY0QQ.jpg",
        "https://www.themoviedb.org/t/p/w1280/8tABCBpzu3mZbzMB3sRzMEHEvJi.jpg",
        "https://www.themoviedb.org/t/p/w1280/bBON9XO9Ek0DjRwMBnJNCwC96Cd.jpg",
        "https://www.themoviedb.org/t/p/w1280/130H1gap9lFfiTF9iDrqNIkFvC9.jpg",
        "https://www.themoviedb.org/t/p/w1280/4Y1WNkd88JXmGfhtWR7dmDAo1T2.jpg",
        "https://www.themoviedb.org/t/p/w1280/qNBAXBIQlnOThrVvA6mA2B5ggV6.jpg",
        "https://www.themoviedb.org/t/p/w1280/NNxYkU70HPurnNCSiCjYAmacwm.jpg",
        "https://www.themoviedb.org/t/p/w1280/h7llKkqkkJtJrTOaDLuVeUYDQ7I.jpg",
        "https://www.themoviedb.org/t/p/w1280/74xTEgt7R36Fpooo50r9T25onhq.jpg",
        "https://www.themoviedb.org/t/p/w1280/vqslbmy5xvnNf5uGcvwWAqq4us4.jpg",
        "https://www.themoviedb.org/t/p/w1280/8E7mIpEpSATxX5JEuw55GYx9hfk.jpg",
        "https://www.themoviedb.org/t/p/w1280/q61qEyssk2ku3okWICKArlAdhBn.jpg",
        "https://www.themoviedb.org/t/p/w1280/20djTLqppfBx5WYA67Y300S6aPD.jpg",
        "https://www.themoviedb.org/t/p/w1280/xmbU4JTUm8rsdtn7Y3Fcm30GpeT.jpg",
        "https://www.themoviedb.org/t/p/w1280/gPbM0MK8CP8A174rmUwGsADNYKD.jpg",
        "https://www.themoviedb.org/t/p/w1280/1XSYOP0JjjyMz1irihvWywro82r.jpg",
        "https://www.themoviedb.org/t/p/w1280/lotWiuWuTGlQ94rzBdy6ZmKZnTA.jpg",
        "https://www.themoviedb.org/t/p/w1280/ggcBBYgnwrRAdx2vTthSAZDfrrk.jpg",
        "https://www.themoviedb.org/t/p/w1280/wjOHjWCUE0YzDiEzKv8AfqHj3ir.jpg",
        "https://www.themoviedb.org/t/p/w1280/qsdjk9oAKSQMWs0Vt5Pyfh6O4GZ.jpg",
        "https://www.themoviedb.org/t/p/w1280/A7AoNT06aRAc4SV89Dwxj3EYAgC.jpg",
        "https://www.themoviedb.org/t/p/w1280/wRLQ5N4GkBpFfUjiT1DpC5cqPFi.jpg",
        "https://www.themoviedb.org/t/p/w1280/eeJjd9JU2Mdj9d7nWRFLWlrcExi.jpg",
        "https://www.themoviedb.org/t/p/w1280/b0Ej6fnXAP8fK75hlyi2jKqdhHz.jpg",
        "https://www.themoviedb.org/t/p/w1280/oifhfVhUcuDjE61V5bS5dfShQrm.jpg",
        "https://www.themoviedb.org/t/p/w1280/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg",
        "https://www.themoviedb.org/t/p/w1280/v31MsWhF9WFh7Qooq6xSBbmJxoG.jpg",
        "https://www.themoviedb.org/t/p/w1280/kdPMUMJzyYAc4roD52qavX0nLIC.jpg",
        "https://www.themoviedb.org/t/p/w1280/sv1xJUazXeYqALzczSZ3O6nkH75.jpg",
        "https://www.themoviedb.org/t/p/w1280/d9nBoowhjiiYc4FBNtQkPY7c11H.jpg",
        "https://www.themoviedb.org/t/p/w1280/wDWwtvkRRlgTiUr6TyLSMX8FCuZ.jpg",
        "https://www.themoviedb.org/t/p/w1280/gD72DhJ7NbfxvtxGiAzLaa0xaoj.jpg",
        "https://www.themoviedb.org/t/p/w1280/62HCnUTziyWcpDaBO2i1DX17ljH.jpg",
        "https://www.themoviedb.org/t/p/w1280/hm58Jw4Lw8OIeECIq5qyPYhAeRJ.jpg",
        "https://www.themoviedb.org/t/p/w1280/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg",
        "https://www.themoviedb.org/t/p/w1280/51tqzRtKMMZEYUpSYkrUE7v9ehm.jpg",
        "https://www.themoviedb.org/t/p/w1280/wToO8opxkGwKgSfJ1JK8tGvkG6U.jpg",
        "https://www.themoviedb.org/t/p/w1280/r2J02Z2OpNTctfOSN1Ydgii51I3.jpg",
        "https://www.themoviedb.org/t/p/w1280/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
        "https://www.themoviedb.org/t/p/w1280/A4j8S6moJS2zNtRR8oWF08gRnL5.jpg",
        "https://www.themoviedb.org/t/p/w1280/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg",
        "https://www.themoviedb.org/t/p/w1280/323BP0itpxTsO0skTwdnVmf7YC9.jpg",
        "https://www.themoviedb.org/t/p/w1280/kuf6dutpsT0vSVehic3EZIqkOBt.jpg",
        "https://www.themoviedb.org/t/p/w1280/7WsyChQLEftFiDOVTGkv3hFpyyt.jpg",
        "https://www.themoviedb.org/t/p/w1280/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg"
    ).shuffled()

    fun select(view: RegionView) {
        viewModelScope.launch {
            facade.select(view)
        }
    }

}