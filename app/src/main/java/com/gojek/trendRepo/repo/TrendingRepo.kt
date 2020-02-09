package com.gojek.trendRepo.repo

import com.gojek.trendRepo.model.Repository
import io.reactivex.Observable

/**
 * Interface specifying methods for fetching details
 */
interface TrendingRepo {

    /**
     * method to fetch details of trending repositories
     */
    fun getTrendingRepos(): Observable<List<Repository>>
}