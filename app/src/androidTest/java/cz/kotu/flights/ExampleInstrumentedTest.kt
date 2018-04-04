package cz.kotu.flights

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import cz.kotu.flights.di.FlightsModule
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("cz.kotu.flights", appContext.packageName)
    }

    @Test
    fun fetchData() {
        val blockingGet = FlightsModule().flightsService.getFlights(
            dateFrom = "03/04/2018",
            dateTo = "03/05/2018"
        ).blockingGet()

        println("RESPONSE: $blockingGet")
    }
}
