package movie.core

import movie.core.di.SetupFeatureModule
import movie.core.model.Region
import movie.core.preference.RegionPreference
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class SetupFeatureTest {

    private lateinit var feature: SetupFeature
    private lateinit var preference: RegionPreference

    @Before
    fun prepare() {
        preference = mock()
        feature = SetupFeatureModule().feature(preference)
    }

    @Test
    fun requiresSetup_returnsTrue() {
        whenever(preference.id).thenThrow(IllegalArgumentException())
        whenever(preference.domain).thenReturn("")
        assertTrue(feature.requiresSetup)
    }

    @Test
    fun requiresSetup_returnsTrue_reverse() {
        whenever(preference.id).thenReturn(0)
        whenever(preference.domain).thenThrow(IllegalArgumentException())
        assertTrue(feature.requiresSetup)
    }

    @Test
    fun requiresSetup_returnsFalse() {
        whenever(preference.id).thenReturn(0)
        whenever(preference.domain).thenReturn("")
        assertFalse(feature.requiresSetup)
    }

    @Test
    fun region_returnsDefaultValue() {
        whenever(preference.id).thenThrow(IllegalArgumentException())
        whenever(preference.domain).thenThrow(IllegalArgumentException())
        assertIs<Region.Custom>(feature.region)
    }

    @Test
    fun region_returnsValue() {
        whenever(preference.id).thenReturn(10101)
        whenever(preference.domain).thenReturn("https://www.cinemacity.cz")
        assertIs<Region.Czechia>(feature.region)
    }

    @Test
    fun region_writesValues_cz() {
        feature.region = Region.Czechia
        verify(preference).id = 10101
        verify(preference).domain = "https://www.cinemacity.cz"
    }

    @Test
    fun region_writesValues_pl() {
        feature.region = Region.Poland
        verify(preference).id = 10103
        verify(preference).domain = "https://www.cinemacity.pl"
    }

    @Test
    fun region_writesValues_ro() {
        feature.region = Region.Romania
        verify(preference).id = 10107
        verify(preference).domain = "https://www.cinemacity.ro"
    }

    @Test
    fun region_writesValues_hu() {
        feature.region = Region.Hungary
        verify(preference).id = 10102
        verify(preference).domain = "https://www.cinemacity.hu"
    }

    @Test
    fun region_writesValues_sk() {
        feature.region = Region.Slovakia
        verify(preference).id = 10105
        verify(preference).domain = "https://www.cinemacity.sk"
    }

}