package com.gojek.trendRepo.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gojek.trendRepo.model.Repository
import com.gojek.trendRepo.repo.TrendingRepo
import io.reactivex.disposables.CompositeDisposable

/**
 * ViewModel class for the TrendingRepoActivity
 */
class TrendRepoViewModel(private val repo: TrendingRepo) : ViewModel() {
    private val TAG = TrendRepoViewModel::class.java.simpleName
    val fetchedRepoDetails = MutableLiveData<List<Repository>>()
    val compositeDisposable = CompositeDisposable()

    /**
     * method to fetch the details of trending repositories
     */
    fun fetchRepoDetails() {
        val disposable = repo.getTrendingRepos().subscribe( {
            fetchedRepoDetails.postValue(it)
        },
        { t ->
            Log.d(TAG, t.message.toString())
        })
        compositeDisposable.add(disposable)
    }

}