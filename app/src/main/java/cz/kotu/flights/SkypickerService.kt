package cz.kotu.flights

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface SkypickerService {
    @GET("flights?v=2&sort=popularity&asc=0&locale=en&daysInDestinationFrom=&daysInDestinationTo=&affilid=&children=0&infants=0&flyFrom=49.2-16.61-250km&to=anywhere&featureName=aggregateResults&typeFlight=oneway&returnFrom=&returnTo=&one_per_date=0&oneforcity=1&wait_for_refresh=0&adults=1&limit=45")
    fun getFlights(
        @Query("dateFrom") dateFrom: String,
        @Query("dateTo") dateTo: String
    ): Single<ResponseBody>
}