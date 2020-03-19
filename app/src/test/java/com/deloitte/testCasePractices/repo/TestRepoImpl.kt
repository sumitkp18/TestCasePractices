package com.deloitte.testCasePractices.repo

import com.deloitte.testCasePractices.model.Repository
import com.deloitte.testCasePractices.repo.database.DatabaseManager
import com.deloitte.testCasePractices.util.AppRxSchedulers
import com.deloitte.testCasePractices.util.RxNetwork
import io.mockk.*
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.junit.*
import org.mockito.Mockito.*

/**
 * Test cases for [RepoImpl] class
 */
class TestRepoImpl {

    private val testScheduler = TestScheduler()
    private val api = mock(API::class.java)
    private val dbManager = mock(DatabaseManager::class.java)
    private val repo = RepoImpl(api, dbManager)

    @Before
    fun before() {
        mockkObject(RxNetwork)
        mockkObject(AppRxSchedulers)
        every { AppRxSchedulers.network() } returns testScheduler
    }

    @After
    fun after() {
        unmockkAll()
    }

    /**
     * test cases for getTrendingRepos method for fetching data from DB
     */
    @Test
    fun testGetTrendingRepos_fromDB() {
        val mockRepoList: List<Repository> = arrayListOf(mock(Repository::class.java))
        //mocking response for fetching data from DB
        doReturn(mockRepoList).`when`(dbManager).getRepoData()

        val testObserver: TestObserver<List<Repository>> = repo.getTrendingRepos(false).subscribeOn(testScheduler).observeOn(testScheduler).test()
        testScheduler.triggerActions()
        testObserver.assertValue(mockRepoList)
        testObserver.dispose()
    }

    /**
     * test cases for getTrendingRepos method for no data in DB and fetching data from server
     */
    @Test
    fun testGetTrendingRepos_nullFromDB() {
        //mocking response for internet available
        every { RxNetwork.isInternetAvailable().toObservable() } returns Observable.just(true)
        val mockRepoList: List<Repository> = arrayListOf(mock(Repository::class.java))
        //mocking response for fetching data from server
        doReturn(Observable.just(mockRepoList)).`when`(api).getTrendingRepos(URLConstants.REPO_ENDPOINT)
        //mocking null response on fetching data from DB
        doReturn(null).`when`(dbManager).getRepoData()

        val testObserver: TestObserver<List<Repository>> = repo.getTrendingRepos(false).subscribeOn(testScheduler).observeOn(testScheduler).test()
        testScheduler.triggerActions()
        testObserver.assertValue(mockRepoList)
        testObserver.dispose()
    }

    /**
     * test cases for getTrendingRepos method for throwing error observable when
     * there is no data in DB and no internet availability
     */
    @Test
    fun testGetTrendingRepos_nullFromDBAndNoInternet() {
        //mocking response for internet not available
        every { RxNetwork.isInternetAvailable().toObservable() } returns Observable.just(false)
        //mocking null response on fetching data from DB
        doReturn(null).`when`(dbManager).getRepoData()

        val testObserver: TestObserver<List<Repository>> = repo.getTrendingRepos(false).subscribeOn(testScheduler).observeOn(testScheduler).test()
        testScheduler.triggerActions()
        testObserver.assertError(Throwable::class.java)
        testObserver.dispose()
    }

    /**
     * test cases for getTrendingRepos method for successfully fetching data from server
     */
    @Test
    fun testGetTrendingRepos_fromServer() {
        //mocking response for internet available
        every { RxNetwork.isInternetAvailable().toObservable() } returns Observable.just(true)
        val mockRepoList: List<Repository> = arrayListOf(mock(Repository::class.java))
        //mocking response for fetching data from server
        doReturn(Observable.just(mockRepoList)).`when`(api).getTrendingRepos(URLConstants.REPO_ENDPOINT)

        val testObserver: TestObserver<List<Repository>> = repo.getTrendingRepos(true).subscribeOn(testScheduler).observeOn(testScheduler).test()
        testScheduler.triggerActions()
        testObserver.assertValue(mockRepoList)
        testObserver.dispose()
    }

    /**
     * test cases for getTrendingRepos method for throwing error observable
     * when there is no internet availability
     */
    @Test
    fun testGetTrendingRepos_fromServerButNoInternet() {
        //mocking response for internet not available
        every { RxNetwork.isInternetAvailable().toObservable() } returns Observable.just(false)

        val testObserver: TestObserver<List<Repository>> = repo.getTrendingRepos(true).subscribeOn(testScheduler).observeOn(testScheduler).test()
        testScheduler.triggerActions()
        testObserver.assertError(Throwable::class.java)
        testObserver.dispose()
    }

}