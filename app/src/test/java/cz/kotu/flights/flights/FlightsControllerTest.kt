package cz.kotu.flights.flights

import com.nhaarman.mockito_kotlin.*
import cz.kotu.flights.SkypickerService
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import org.threeten.bp.format.DateTimeFormatter
import java.io.IOException

class FlightsControllerTest {
    private lateinit var repository: SimpleFlightsRepository
    private val dateFormatter = mock<DateTimeFormatter> {
        whenever(it.format(any())).thenReturn("any/date")
    }

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        repository = SimpleFlightsRepository()
    }

    @Test
    fun `do not download on the same day`() {
        val flightsService = mock<SkypickerService>()
        val sameDay = LocalDate.of(2018, Month.APRIL, 5)
        repository.offersDate.set(sameDay)
        val controller = FlightsController(
            dateFormatter,
            flightsService,
            repository
        )
        controller.checkUpdate(sameDay)
        verify(flightsService, never()).getFlights(any(), any())
    }

    @Test
    fun `download new offers`() {
        val flights = listOf(
            simpleFlight("1")
        )
        val flightsService = mock<SkypickerService> {
            whenever(it.getFlights(any(), any())).thenReturn(
                Single.just(
                    FlightsResponse(
                        "ANY", flights
                    )
                )
            )
        }
        val controller = FlightsController(
            dateFormatter,
            flightsService,
            repository
        )
        val today = LocalDate.of(2018, Month.APRIL, 5)
        controller.checkUpdate(today)
        controller.flights.hide().test().assertValue(flights)
        Assert.assertEquals(today, repository.offersDate.get())
    }

    @Test
    fun `download new 5 offers and exclude previous`() {
        val flightsService = mock<SkypickerService> {
            whenever(it.getFlights(any(), any())).thenReturn(
                Single.just(
                    FlightsResponse(
                        "ANY", listOf(
                            simpleFlight("1"),
                            simpleFlight("2"),
                            simpleFlight("A"),
                            simpleFlight("3"),
                            simpleFlight("4"),
                            simpleFlight("B"),
                            simpleFlight("5"),
                            simpleFlight("6"),
                            simpleFlight("C")
                        )
                    )
                )
            )
        }
        repository.previousFlights.set(
            listOf(
                simpleFlight("A"),
                simpleFlight("B"),
                simpleFlight("C")
            )
        )
        val controller = FlightsController(
            dateFormatter,
            flightsService,
            repository
        )
        controller.checkUpdate(LocalDate.of(2018, Month.APRIL, 5))
        controller.flights.hide().test().assertValue(
            listOf(
                simpleFlight("1"),
                simpleFlight("2"),
                simpleFlight("3"),
                simpleFlight("4"),
                simpleFlight("5")
            )
        )
    }

    @Test
    fun `show error on failed download and no offers`() {
        val flightsService = mock<SkypickerService> {
            whenever(it.getFlights(any(), any())).thenReturn(
                Single.error(IOException("Something wrong"))
            )
        }
        val controller = FlightsController(
            dateFormatter,
            flightsService,
            repository
        )
        controller.checkUpdate(LocalDate.of(2018, Month.APRIL, 5))
        controller.flights.hide().test().assertValue(listOf())
        controller.message.hide().test().assertValue("java.io.IOException: Something wrong")
    }

    fun simpleFlight(id: String): Flight = Flight(id, "any", 0.0, "any", 0.0)
}