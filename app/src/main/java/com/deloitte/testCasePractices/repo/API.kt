package com.deloitte.testCasePractices.repo

import com.deloitte.testCasePractices.model.Repository
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Interface specifying various api calls for repository details
 * to be used by retrofit
 */
interface API {

    /**
     * API to fetch details of trending repositories
     * @param url endpoint for fetching trending repositories
     */
    @GET
    fun getTrendingRepos(@Url url: String): Observable<List<Repository>>
}