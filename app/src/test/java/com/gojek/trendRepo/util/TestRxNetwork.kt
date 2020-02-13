package com.gojek.trendRepo.util

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.InternetObservingSettings
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.strategy.SocketInternetObservingStrategy
import com.gojek.trendRepo.repo.URLConstants
import io.mockk.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.*
import org.mockito.*
import org.mockito.Mockito.*
import org.reactivestreams.Subscriber

class TestRxNetwork {

    private val testScheduler = TestScheduler()

    @Before
    fun before() {
        mockkStatic(ReactiveNetwork::class)
        mockkObject(AppRxSchedulers)
        every { ReactiveNetwork.checkInternetConnectivity(getInternetObservingSettings()) } returns (Single.just(true))
        every { AppRxSchedulers.network() } returns testScheduler
    }

    private fun getInternetObservingSettings(host: String? = null): InternetObservingSettings {
        return InternetObservingSettings.builder()
            .host(host ?: RxNetwork.DEFAULT_HOST)
            .strategy(SocketInternetObservingStrategy())
            .build()
    }

    @Test
    fun testIsInternetAvailable() {
        val testObserver = RxNetwork.isInternetAvailable().test()
        testScheduler.triggerActions()
        testObserver.assertValue(true)
        testObserver.dispose()
    }

    @Test
    fun testGetNetworkNotAvailable() {
        val errorMessage = "Network not available"
        val testObserver = RxNetwork.getNetworkNotAvailable(errorMessage, List::class.java).test()
        testObserver.assertError(Throwable::class.java)
        Assert.assertEquals(testObserver.errors().first().localizedMessage, errorMessage)
        testObserver.dispose()
    }
}