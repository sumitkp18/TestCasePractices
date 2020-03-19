package com.deloitte.testCasePractices.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.deloitte.testCasePractices.model.Repository
import com.deloitte.testCasePractices.repo.Repo
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import org.junit.*
import org.junit.rules.*
import org.mockito.Mockito.*

/**
 * Test cases for [FetchViewModel]
 */
class TestFetchViewModel {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private var jsonFileName = "/trendingRepositories.json"
    private lateinit var mockResponse: JsonArray
    private lateinit var mockRepoList: List<Repository>
    private val repo = mock(Repo::class.java)
    private var fetchViewModel = FetchViewModel(repo)

    @Before
    fun before() {
        mockResponse = getConfigJsonFromFile(jsonFileName)
        mockRepoList = Gson().fromJson<List<Repository>>(mockResponse, object: TypeToken<List<Repository>>() {}.type)
    }

    /**
     * get config JSONObject from config file
     * */
    private fun getConfigJsonFromFile(fileName: String): JsonArray {
        val file = TestFetchViewModel::class.java.getResource(fileName)?.readText()
        return JsonParser().parse(file).asJsonArray
    }

    /**
     * test case for success scenario on fetching data
     */
    @Test
    fun testFetchRepoDetails_forSuccess() {
        //mocking success response for call to repo for fetching data
        `when`(repo.getTrendingRepos(true)).thenReturn(Observable.just(mockRepoList))
        fetchViewModel.fetchRepoDetails(true)
        val actualValue = fetchViewModel.fetchedRepoDetails.value
        Assert.assertEquals(mockRepoList, actualValue)
    }

    /**
     * test case for error scenario on fetching data
     */
    @Test
    fun testFetchRepoDetails_forError() {
        //mocking error response for call to repo for fetching data
        `when`(repo.getTrendingRepos(true)).thenReturn(Observable.just(Throwable("Error")) as Observable<List<Repository>>)
        Assert.assertEquals(false, fetchViewModel.networkError.get())
        fetchViewModel.fetchRepoDetails(true)
        Assert.assertNull(fetchViewModel.fetchedRepoDetails.value)
        val actualValue = fetchViewModel.networkError.get()
        Assert.assertEquals(true, actualValue)
    }
}