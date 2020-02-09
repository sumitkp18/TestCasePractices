package com.gojek.trendRepo.repo

import com.gojek.trendRepo.model.Repository
import io.reactivex.Observable

/**
 * Interface specifying methods for fetching details
 */
interface TrendingRepo {

    /**
     * method to fetch details of trending repositories
     * @param fetchFromServer a boolean which tells whether to fetch from cache or server
     */
    fun getTrendingRepos(fetchFromServer: Boolean = false): Observable<List<Repository>>
}