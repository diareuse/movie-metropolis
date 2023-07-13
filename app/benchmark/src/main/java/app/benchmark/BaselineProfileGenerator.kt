package app.benchmark

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test

@RequiresApi(Build.VERSION_CODES.P)
class BaselineProfileGenerator {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun appStartupAndUserJourneys() {
        baselineProfileRule.collect(
            packageName = "movie.metropolis.app",
            maxIterations = 1,
            stableIterations = 1
        ) {
            if ((iteration ?: 1) <= 1) {
                startActivityAndWait()
                device.findObject(By.text("Česko")).click()
                device.wait(Until.findObject(By.res("listingColumn")), 2000)
            }
            device.findObject(By.res("listingColumn")).also {
                it.fling(Direction.DOWN)
            }
            device.pressBack()
        }
    }
}
