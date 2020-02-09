package com.gojek.trendRepo.repo

import com.gojek.trendRepo.model.Repository
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Interface specifying various api calls for repository details
 * to be used by retrofit
 */
interface TrendingRepoAPI {

    /**
     * API to fetch details of trending repositories
     */
    @GET
    fun getTrendingRepos(@Url url: String): Observable<List<Repository>>
}