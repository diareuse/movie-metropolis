package movie.metropolis.app.presentation.setup

import kotlinx.coroutines.test.runTest
import movie.core.model.Region
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.model.adapter.RegionViewFromFeature
import movie.metropolis.app.presentation.FeatureTest
import org.junit.Test
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Locale
import kotlin.test.assertContentEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SetupFacadeTest : FeatureTest() {

    private lateinit var facade: SetupFacade

    override fun prepare() {
        facade = FacadeModule().setup(setup)
    }

    @Test
    fun hasRegions_inOrder() = runTest {
        val regions = facade.getRegions().getOrThrow()
        assertContentEquals(
            listOf("Česko", "Slovensko", "Polska", "Magyarország", "România"),
            regions.map { it.name }
        )
    }

    @Test
    fun requiresSetup_returnsTrue() {
        whenever(setup.requiresSetup).thenReturn(true)
        assertTrue(facade.requiresSetup)
    }

    @Test
    fun requiresSetup_returnsFalse() {
        whenever(setup.requiresSetup).thenReturn(false)
        assertFalse(facade.requiresSetup)
    }

    @Test
    fun select_callsFeature() = runTest {
        val region = Region.Poland
        facade.select(RegionViewFromFeature(region, Locale.getDefault()))
        verify(setup).region = region
    }

}