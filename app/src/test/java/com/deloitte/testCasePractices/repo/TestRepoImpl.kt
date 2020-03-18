package com.deloitte.testCasePractices.repo

import com.deloitte.testCasePractices.model.Repository
import com.deloitte.testCasePractices.repo.database.DatabaseManager
import com.deloitte.testCasePractices.util.AppRxSchedulers
import com.deloitte.testCasePractices.util.RxNetwork
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import io.mockk.*
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.junit.*
import org.mockito.Mockito.*

class TestRepoImpl {

    private var mockJson = "/trendingRepositories.json"
    private lateinit var mockResponse: JsonArray
    private val testScheduler = TestScheduler()
    private val api = mock(API::class.java)
    private val dbManager = mock(DatabaseManager::class.java)
    private val repo = RepoImpl(api, dbManager)

    @Before
    fun before() {
        mockResponse = getConfigJsonFromFile(mockJson)
        mockkObject(RxNetwork)
        mockkObject(AppRxSchedulers)
        every { AppRxSchedulers.network() } returns testScheduler
    }

    @After
    fun after() {
        unmockkAll()
    }


    /**
     * get config JSONObject from config file
     * */
    private fun getConfigJsonFromFile(fileName: String): JsonArray {
        val file = TestRepoImpl::class.java.getResource(fileName)!!.readText()
        return JsonParser().parse(file).asJsonArray

    }



    @Test
    fun testGetTrendingRepos_fromDB() {
        val mockRepoList = Gson().fromJson<List<Repository>>(mockResponse, object: TypeToken<List<Repository>>() {}.type)

//        doReturn(Observable.just(mockRepoList)).`when`(api).getTrendingRepos(URLConstants.REPO_ENDPOINT)
        doReturn(mockRepoList).`when`(dbManager).getRepoData()

        val testObserver: TestObserver<List<Repository>> = repo.getTrendingRepos(false).subscribeOn(testScheduler).observeOn(testScheduler).test()
        testScheduler.triggerActions()
        testObserver.assertValue(mockRepoList)
        testObserver.dispose()
    }

    @Test
    fun testGetTrendingRepos_nullFromDB() {
        every { RxNetwork.isInternetAvailable().toObservable() } returns Observable.just(true)
        val mockRepoList = Gson().fromJson<List<Repository>>(mockResponse, object: TypeToken<List<Repository>>() {}.type)

        doReturn(Observable.just(mockRepoList)).`when`(api).getTrendingRepos(URLConstants.REPO_ENDPOINT)
        doReturn(null).`when`(dbManager).getRepoData()

        val testObserver: TestObserver<List<Repository>> = repo.getTrendingRepos(false).subscribeOn(testScheduler).observeOn(testScheduler).test()
        testScheduler.triggerActions()
        testObserver.assertValue(mockRepoList)
        testObserver.dispose()
    }

    @Test
    fun testGetTrendingRepos_nullFromDBAndNoInternet() {
        every { RxNetwork.isInternetAvailable().toObservable() } returns Observable.just(false)
        val mockRepoList = Gson().fromJson<List<Repository>>(mockResponse, object: TypeToken<List<Repository>>() {}.type)

        doReturn(Observable.just(mockRepoList)).`when`(api).getTrendingRepos(URLConstants.REPO_ENDPOINT)
        doReturn(null).`when`(dbManager).getRepoData()

        val testObserver: TestObserver<List<Repository>> = repo.getTrendingRepos(false).subscribeOn(testScheduler).observeOn(testScheduler).test()
        testScheduler.triggerActions()
        testObserver.assertError(Throwable::class.java)
        testObserver.dispose()
    }

    @Test
    fun testGetTrendingRepos_fromServer() {
        every { RxNetwork.isInternetAvailable().toObservable() } returns Observable.just(true)
        val mockRepoList = Gson().fromJson<List<Repository>>(mockResponse, object: TypeToken<List<Repository>>() {}.type)

        doReturn(Observable.just(mockRepoList)).`when`(api).getTrendingRepos(URLConstants.REPO_ENDPOINT)
        doReturn(null).`when`(dbManager).getRepoData()

        val testObserver: TestObserver<List<Repository>> = repo.getTrendingRepos(true).subscribeOn(testScheduler).observeOn(testScheduler).test()
        testScheduler.triggerActions()
        testObserver.assertValue(mockRepoList)
        testObserver.dispose()
    }

    @Test
    fun testGetTrendingRepos_fromServerButNoInternet() {
        every { RxNetwork.isInternetAvailable().toObservable() } returns Observable.just(false)
        val mockRepoList = Gson().fromJson<List<Repository>>(mockResponse, object: TypeToken<List<Repository>>() {}.type)

        doReturn(Observable.just(mockRepoList)).`when`(api).getTrendingRepos(URLConstants.REPO_ENDPOINT)
        doReturn(null).`when`(dbManager).getRepoData()

        val testObserver: TestObserver<List<Repository>> = repo.getTrendingRepos(true).subscribeOn(testScheduler).observeOn(testScheduler).test()
        testScheduler.triggerActions()
        testObserver.assertError(Throwable::class.java)
        testObserver.dispose()
    }

}