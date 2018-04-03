package cz.kotu.flights

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

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

        val service = retrofit.create(SkypickerService::class.java)

        val blockingGet = service.getFlights(
            dateFrom = "03/04/2018",
            dateTo = "03/05/2018"
        ).blockingGet()

        println("RESPONSE: $blockingGet")
    }
}
