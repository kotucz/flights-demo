package cz.kotu.flights.flights

import com.f2prateek.rx.preferences2.Preference
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import org.threeten.bp.LocalDate

class SimpleFlightsRepository : FlightsRepository {
    override val offersDate = simplePreference<LocalDate>(LocalDate.MIN)
    override val offersFlights = simplePreference<List<Flight>>(listOf())
    override val previousFlights = simplePreference<List<Flight>>(listOf())

    private inline fun <reified T : Any> simplePreference(default: T): Preference<T> {
        return object : Preference<T> {
            val inner = BehaviorRelay.createDefault<T>(default)

            override fun isSet(): Boolean {
                TODO("not implemented")
            }

            override fun key(): String {
                TODO("not implemented")
            }

            override fun asObservable(): Observable<T> {
                return inner.hide()
            }

            override fun asConsumer(): Consumer<in T> {
                return inner
            }

            override fun defaultValue(): T {
                return defaultValue()
            }

            override fun get(): T {
                return inner.value
            }

            override fun set(value: T) {
                inner.accept(value)
            }

            override fun delete() {
                inner.accept(default)
            }
        }
    }
}