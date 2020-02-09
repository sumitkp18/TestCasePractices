package com.gojek.trendRepo.repo

import com.gojek.trendRepo.model.Repository
import com.gojek.trendRepo.repo.database.TrendRepoDatabaseManager
import com.gojek.trendRepo.util.AppRxSchedulers
import com.gojek.trendRepo.util.RxNetwork
import io.reactivex.Observable

/**
 * Implementation class of the TrendingRepo Interface
 */
class TrendingRepoImpl(private val trendingRepoAPI: TrendingRepoAPI) : TrendingRepo {

    /**
     * Method to fetch details of trending repositories.
     * Based on Internet connectivity it fetches data from API call or local cache
     */
    override fun getTrendingRepos(fetchFromServer: Boolean): Observable<List<Repository>> {
        if (fetchFromServer) {
            return RxNetwork.isInternetAvailable().toObservable().concatMap { isInternetAvailable ->
                if (isInternetAvailable) {
                    trendingRepoAPI.getTrendingRepos(URLConstants.REPO_ENDPOINT)
                        .subscribeOn(AppRxSchedulers.network()).concatMap {
                            TrendRepoDatabaseManager.insertRepoData(it)
                            Observable.just(it)
                        }
                } else {
                    RxNetwork.getNetworkNotAvailable(type = List::class.java) as Observable<List<Repository>>
                }
            }
        } else {
            TrendRepoDatabaseManager.getRepoData()?.let {
                return Observable.just(it)
            }
            return getTrendingRepos(true)
        }
    }

}