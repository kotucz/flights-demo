package cz.kotu.flights

import com.nhaarman.mockito_kotlin.mock
import cz.kotu.flights.di.FlightsModule
import org.junit.Assert.assertEquals
import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.Month

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun formatDate() {
        val date = LocalDate.of(2018, Month.APRIL, 1)
        assertEquals("01/04/2018", FlightsModule(mock()).dateFormatter.format(date))
    }
}
