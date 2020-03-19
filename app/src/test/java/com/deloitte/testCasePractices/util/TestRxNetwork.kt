package com.deloitte.testCasePractices.util

import io.mockk.*
import io.reactivex.schedulers.TestScheduler
import org.junit.*

/**
 * Test cases for RxNetwork class
 */
class TestRxNetwork {

    private val testScheduler = TestScheduler()

    @Before
    fun before() {
        mockkObject(AppRxSchedulers)
        every { AppRxSchedulers.network() } returns testScheduler
    }

    /**
     * test case for isInternetAvailable method with a default host
     */
    @Test
    fun testIsInternetAvailable_withDefaultHost() {
        val testObserver = RxNetwork.isInternetAvailable().test()
        testScheduler.triggerActions()
        testObserver.assertValue(true)
        testObserver.dispose()
    }

    /**
     * Test case for isInternetAvailable with specifying a host
     */
    @Test
    fun testIsInternetAvailable_withSpecifiedHost() {
        val host = "www.bing.com"
        val testObserver = RxNetwork.isInternetAvailable(host).test()
        testScheduler.triggerActions()
        testObserver.assertValue(true)
        testObserver.dispose()
    }

    /**
     * test case for getNetworkNotAvailable method to fetch an error observable with a specified error message
     */
    @Test
    fun testGetNetworkNotAvailable_withSpecifiedMessage() {
        val errorMessage = "Network not available"
        val testObserver =
            RxNetwork.getNetworkNotAvailable(errorMessage, List::class.java).test()
        testObserver.assertError(Throwable::class.java)
        Assert.assertEquals(testObserver.errors().first().localizedMessage, errorMessage)
        testObserver.dispose()
    }

    /**
     * test case for getNetworkNotAvailable method to fetch an error observable with default error message
     */
    @Test
    fun testGetNetworkNotAvailable_withDefaultMessage() {
        val testObserver =
            RxNetwork.getNetworkNotAvailable(type = List::class.java).test()
        testObserver.assertError(Throwable::class.java)
        Assert.assertEquals(testObserver.errors().first().localizedMessage, RxNetwork.NETWORK_NOT_AVAILABLE)
        testObserver.dispose()
    }
}