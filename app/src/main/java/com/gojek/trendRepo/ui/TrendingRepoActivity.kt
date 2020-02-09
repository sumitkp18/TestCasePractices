package com.gojek.trendRepo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gojek.trendRepo.R
import com.gojek.trendRepo.databinding.ActivityTrendingRepoBinding
import com.gojek.trendRepo.model.Repository
import kotlinx.android.synthetic.main.activity_trending_repo.no_network_layout
import kotlinx.android.synthetic.main.activity_trending_repo.rv_repo
import kotlinx.android.synthetic.main.activity_trending_repo.shimmer_view_container
import kotlinx.android.synthetic.main.layout_no_network.view.retry_btn
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Activity class to show the list of trending repositories
 */
class TrendingRepoActivity : AppCompatActivity() {

    private val viewModel by viewModel<TrendRepoViewModel>()
    private lateinit var viewBinding: ActivityTrendingRepoBinding
    private val listAdapter = ListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_trending_repo)
        viewBinding.networkError = viewModel.networkError
        initObservers()
        initListeners()
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

    private fun initListeners() {
        no_network_layout.retry_btn.setOnClickListener {
            shimmer_view_container.startShimmerAnimation()
            viewModel.fetchRepoDetails()
        }
    }

    private fun setAdapter(repoList: List<Repository>?) {
        repoList?.let {
            listAdapter.setData(it)
            listAdapter.notifyDataSetChanged()
            shimmer_view_container.stopShimmerAnimation()
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
