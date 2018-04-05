package cz.kotu.flights.di

import android.app.Application
import cz.kotu.flights.SkypickerService
import cz.kotu.flights.data.PreferencesFlightsRepository
import cz.kotu.flights.flights.FlightsController
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class FlightsModule(context: Application) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.skypicker.com/")
        .client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val flightsService = retrofit.create(SkypickerService::class.java)

    val flightsRepository by lazy { PreferencesFlightsRepository(context) }

    val flightsController by lazy {
        FlightsController(
            context.resources,
            dateFormatter,
            flightsService,
            flightsRepository
        )
    }
}