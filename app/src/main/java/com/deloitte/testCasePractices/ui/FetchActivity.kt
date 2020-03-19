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
import com.deloitte.testCasePractices.adapter.ListAdapter
import com.deloitte.testCasePractices.databinding.ActivityFetchBinding
import com.deloitte.testCasePractices.model.Repository
import kotlinx.android.synthetic.main.activity_fetch.no_network_layout
import kotlinx.android.synthetic.main.activity_fetch.rv_repo
import kotlinx.android.synthetic.main.activity_fetch.shimmer_view_container
import kotlinx.android.synthetic.main.activity_fetch.swipe_refresh
import kotlinx.android.synthetic.main.layout_no_network.view.retry_btn
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Activity class to show the list of trending repositories
 */
class FetchActivity : AppCompatActivity() {

    private val viewModel by viewModel<FetchViewModel>()
    private lateinit var viewBinding: ActivityFetchBinding
    private val listAdapter = ListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_fetch)
        viewBinding.networkError = viewModel.networkError

        //setup
        setupActionbar()
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

    /**
     * Setting up action bar
     */
    private fun setupActionbar() {
        //custom view on action bar for center aligned title
        supportActionBar?.run {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.layout_action_bar_title)
            elevation = resources.getDimensionPixelSize(R.dimen.space_zero).toFloat()
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
            with(shimmer_view_container) {
                startShimmerAnimation()
                visibility = View.VISIBLE
            }
            viewModel.fetchRepoDetails(fetchFromServer = true)
        }
        swipe_refresh.setOnRefreshListener {
            with(listAdapter) {
                clearData()
                notifyDataSetChanged()
            }
            with(shimmer_view_container) {
                visibility = View.VISIBLE
                startShimmerAnimation()
            }
            swipe_refresh.isRefreshing = false
            viewModel.fetchRepoDetails(true)
        }
    }

    /**
     * sets data in the list adapter of recycler view
     */
    private fun setAdapter(repoList: List<Repository>?) {
        repoList?.let {
            with(listAdapter) {
                setData(it)
                notifyDataSetChanged()
            }
            with(shimmer_view_container) {
                visibility = View.GONE
                stopShimmerAnimation()
            }
        }
    }

    /**
     * Setup for the recycler View
     */
    private fun setUpRecyclerView() {
        with(rv_repo) {
            layoutManager = LinearLayoutManager(this@FetchActivity)
            adapter = listAdapter
        }
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
