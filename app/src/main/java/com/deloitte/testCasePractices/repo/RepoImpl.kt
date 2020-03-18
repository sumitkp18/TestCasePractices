package com.deloitte.testCasePractices.repo

import com.deloitte.testCasePractices.model.Repository
import com.deloitte.testCasePractices.repo.database.DatabaseManager
import com.deloitte.testCasePractices.util.AppRxSchedulers
import com.deloitte.testCasePractices.util.RxNetwork
import io.reactivex.Observable

/**
 * Implementation class of the [Repo] Interface
 */
class RepoImpl(private val API: API) : Repo {

    /**
     * Method to fetch details of trending repositories.
     * Based on Internet connectivity it fetches data from API call or local cache
     */
    override fun getTrendingRepos(fetchFromServer: Boolean): Observable<List<Repository>> {
        if (fetchFromServer) {
            return RxNetwork.isInternetAvailable().toObservable().concatMap { isInternetAvailable ->
                if (isInternetAvailable) {
                    API.getTrendingRepos(URLConstants.REPO_ENDPOINT)
                        .subscribeOn(AppRxSchedulers.network()).concatMap {
                            insertToDB(it)
                            Observable.just(it)
                        }
                } else {
                    RxNetwork.getNetworkNotAvailable(type = List::class.java) as Observable<List<Repository>>
                }
            }
        } else {
            return getTrendingReposFromDB() ?: getTrendingRepos(true)
        }
    }

    fun insertToDB(listOfRepos: List<Repository>) {
        DatabaseManager.insertRepoData(listOfRepos)
    }

    fun getTrendingReposFromDB(): Observable<List<Repository>>? {
        return DatabaseManager.getRepoData()?.let {
            Observable.just(it)
        }
    }

}