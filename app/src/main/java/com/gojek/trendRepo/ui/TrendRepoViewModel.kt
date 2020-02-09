package com.gojek.trendRepo.ui

import android.util.Log
import androidx.databinding.ObservableBoolean
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
    val networkError  = ObservableBoolean()
    val compositeDisposable = CompositeDisposable()

    /**
     * method to fetch the details of trending repositories
     */
    fun fetchRepoDetails() {
        networkError.set(false)
        val disposable = repo.getTrendingRepos().subscribe( {
            fetchedRepoDetails.postValue(it)
        },
        { t ->
            networkError.set(true)
            Log.d(TAG, t.message.toString())
        })
        compositeDisposable.add(disposable)
    }

}