package com.deloitte.testCasePractices.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.deloitte.testCasePractices.R
import com.deloitte.testCasePractices.databinding.ActivityTrendingRepoBinding
import com.deloitte.testCasePractices.model.Repository
import kotlinx.android.synthetic.main.activity_trending_repo.no_network_layout
import kotlinx.android.synthetic.main.activity_trending_repo.rv_repo
import kotlinx.android.synthetic.main.activity_trending_repo.shimmer_view_container
import kotlinx.android.synthetic.main.activity_trending_repo.swipe_refresh
import kotlinx.android.synthetic.main.layout_no_network.view.retry_btn
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Activity class to show the list of trending repositories
 */
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<FetchViewModel>()
    private lateinit var viewBinding: ActivityTrendingRepoBinding
    private val listAdapter = ListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_trending_repo)
        viewBinding.networkError = viewModel.networkError

        //custom view on action bar for center aligned title
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.layout_action_bar_title)
        supportActionBar?.elevation = resources.getDimensionPixelSize(R.dimen.space_zero).toFloat()


        //setup
        initObservers()
        initListeners()
        setUpRecyclerView()

        //fetching data
        if (viewModel.fetchedRepoDetails.value == null) {
            viewModel.fetchRepoDetails()
        } else {
            setAdapter(viewModel.fetchedRepoDetails.value)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.options, menu)
        return true
    }

    /**
     * initializes observers
     */
    private fun initObservers() {
        viewModel.fetchedRepoDetails.observe(this, Observer {
            setAdapter(it)
        })
    }

    /**
     * initializes various listeners
     */
    private fun initListeners() {
        no_network_layout.retry_btn.setOnClickListener {
            shimmer_view_container.startShimmerAnimation()
            shimmer_view_container.visibility = View.VISIBLE
            viewModel.fetchRepoDetails(fetchFromServer = true)
        }
        swipe_refresh.setOnRefreshListener {
            shimmer_view_container.visibility = View.VISIBLE
            listAdapter.clearData()
            listAdapter.notifyDataSetChanged()
            shimmer_view_container.startShimmerAnimation()
            swipe_refresh.isRefreshing = false
            viewModel.fetchRepoDetails(true)
        }
    }

    /**
     * sets data in the list adapter of recycler view
     */
    private fun setAdapter(repoList: List<Repository>?) {
        repoList?.let {
            listAdapter.setData(it)
            listAdapter.notifyDataSetChanged()
            shimmer_view_container.visibility = View.GONE
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
}
