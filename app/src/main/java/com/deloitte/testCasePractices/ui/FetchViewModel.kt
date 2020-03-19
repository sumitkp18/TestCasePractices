package com.deloitte.testCasePractices.ui

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.deloitte.testCasePractices.model.Repository
import com.deloitte.testCasePractices.repo.Repo
import io.reactivex.disposables.CompositeDisposable

/**
 * ViewModel class for the [FetchActivity]
 */
class FetchViewModel(private val repo: Repo) : ViewModel() {
    private val TAG = FetchViewModel::class.java.simpleName
    val fetchedRepoDetails = MutableLiveData<List<Repository>>()
    val networkError  = ObservableBoolean()
    private val compositeDisposable = CompositeDisposable()

    /**
     * method to fetch the details of trending repositories
     */
    fun fetchRepoDetails(fetchFromServer: Boolean = false) {
        networkError.set(false)
        val disposable = repo.getTrendingRepos(fetchFromServer).subscribe( {
            fetchedRepoDetails.postValue(it)
        },
        { t ->
            networkError.set(true)
            Log.d(TAG, t.message.toString())
        })
        compositeDisposable.add(disposable)
    }

    /**
     * clears the [CompositeDisposable]
     */
    override fun onCleared() {
        compositeDisposable.clear()
    }

}