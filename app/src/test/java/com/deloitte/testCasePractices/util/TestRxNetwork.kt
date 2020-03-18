package com.deloitte.testCasePractices.util

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.InternetObservingSettings
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.strategy.SocketInternetObservingStrategy
import io.mockk.*
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.*

class TestRxNetwork {

    private val testScheduler = TestScheduler()

    @Before
    fun before() {
        mockkObject(AppRxSchedulers)
        every { AppRxSchedulers.network() } returns testScheduler
    }

    @Test
    fun testIsInternetAvailable_withDefaultHost() {
        val testObserver = RxNetwork.isInternetAvailable().test()
        testScheduler.triggerActions()
        testObserver.assertValue(true)
        testObserver.dispose()
    }

    @Test
    fun testIsInternetAvailable_withSpecifiedHost() {
        val host = "www.bing.com"
        val testObserver = RxNetwork.isInternetAvailable(host).test()
        testScheduler.triggerActions()
        testObserver.assertValue(true)
        testObserver.dispose()
    }

    @Test
    fun testGetNetworkNotAvailable_withSpecifiedMessage() {
        val errorMessage = "Network not available"
        val testObserver =
            RxNetwork.getNetworkNotAvailable(errorMessage, List::class.java).test()
        testObserver.assertError(Throwable::class.java)
        Assert.assertEquals(testObserver.errors().first().localizedMessage, errorMessage)
        testObserver.dispose()
    }

    @Test
    fun testGetNetworkNotAvailable_withDefaultMessage() {
        val testObserver =
            RxNetwork.getNetworkNotAvailable(type = List::class.java).test()
        testObserver.assertError(Throwable::class.java)
        Assert.assertEquals(testObserver.errors().first().localizedMessage, RxNetwork.NETWORK_NOT_AVAILABLE)
        testObserver.dispose()
    }
}