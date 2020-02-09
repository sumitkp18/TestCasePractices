package com.gojek.trendRepo.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gojek.trendRepo.R
import com.gojek.trendRepo.model.Repository
import kotlinx.android.synthetic.main.activity_trending_repo.rv_repo
import kotlinx.android.synthetic.main.activity_trending_repo.shimmer_view_container
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrendingRepoActivity : AppCompatActivity() {

    private val viewModel by viewModel<TrendRepoViewModel>()
    private val listAdapter = ListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trending_repo)
        shimmer_view_container.visibility = View.VISIBLE
        initObservers()
        setUpRecyclerView()
        if (viewModel.fetchedRepoDetails.value == null) {
            viewModel.fetchRepoDetails()
        } else {
            setAdapter(viewModel.fetchedRepoDetails.value)
        }
    }

    private fun initObservers() {
        viewModel.fetchedRepoDetails.observe(this, Observer {
            setAdapter(it)
        })
    }

    private fun setAdapter(repoList: List<Repository>?) {
        repoList?.let {
            listAdapter.setData(it)
            listAdapter.notifyDataSetChanged()
            shimmer_view_container.visibility = View.GONE
        }
    }

    /**
     * Setup for the recycler View
     *
     */
    private fun setUpRecyclerView() {
        rv_repo.layoutManager = LinearLayoutManager(this)
        rv_repo.adapter = listAdapter
    }

    public override fun onResume() {
        super.onResume()
        shimmer_view_container.startShimmerAnimation()
    }

    override fun onPause() {
        shimmer_view_container.stopShimmerAnimation()
        super.onPause()
    }

    override fun onDestroy() {
        viewModel.compositeDisposable.clear()
        super.onDestroy()
    }
}
