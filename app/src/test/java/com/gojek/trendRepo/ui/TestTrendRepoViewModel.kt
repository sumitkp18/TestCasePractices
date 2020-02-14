package com.gojek.trendRepo.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gojek.trendRepo.model.Repository
import com.gojek.trendRepo.repo.TrendingRepo
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import org.junit.*
import org.junit.rules.*
import org.mockito.Mockito.*

class TestTrendRepoViewModel {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private var jsonFileName = "/trendingRepositories.json"
    private lateinit var mockResponse: JsonArray
    private lateinit var mockRepoList: List<Repository>
    private val repo = mock(TrendingRepo::class.java)
    private var trendRepoViewModel = TrendRepoViewModel(repo)

    @Before
    fun before() {
        mockResponse = getConfigJsonFromFile(jsonFileName)
        mockRepoList = Gson().fromJson<List<Repository>>(mockResponse, object: TypeToken<List<Repository>>() {}.type)
    }

    /**
     * get config JSONObject from config file
     * */
    private fun getConfigJsonFromFile(fileName: String): JsonArray {
        val file = TestTrendRepoViewModel::class.java.getResource(fileName)?.readText()
        return JsonParser().parse(file).asJsonArray
    }

    @Test
    fun testFetchRepoDetails_forSuccess() {
        `when`(repo.getTrendingRepos(true)).thenReturn(Observable.just(mockRepoList))
        trendRepoViewModel.fetchRepoDetails(true)
        val actualValue = trendRepoViewModel.fetchedRepoDetails.value
        Assert.assertEquals(mockRepoList, actualValue)
    }

    @Test
    fun tstFetchRepoDetails_forError() {
        `when`(repo.getTrendingRepos(true)).thenReturn(Observable.just(Throwable("Error")) as Observable<List<Repository>>)
        Assert.assertEquals(false, trendRepoViewModel.networkError.get())
        trendRepoViewModel.fetchRepoDetails(true)
        Assert.assertNull(trendRepoViewModel.fetchedRepoDetails.value)
        val actualValue = trendRepoViewModel.networkError.get()
        Assert.assertEquals(true, actualValue)
    }
}