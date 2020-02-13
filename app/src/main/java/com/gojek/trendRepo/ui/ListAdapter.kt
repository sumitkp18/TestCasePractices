package com.gojek.trendRepo.ui

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gojek.trendRepo.R
import com.gojek.trendRepo.databinding.ItemRepoBinding
import com.gojek.trendRepo.model.Repository
import com.gojek.trendRepo.ui.ListAdapter.RepoItemViewHolder
import com.squareup.picasso.Picasso

/**
 * Adapter class for the recycler view to show the details of trending repositories
 */
class ListAdapter : RecyclerView.Adapter<RepoItemViewHolder>() {

    private val dataSet: ArrayList<Repository> = arrayListOf()
    private var expandedItemPosition: Int? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): RepoItemViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
        val binding: ItemRepoBinding = DataBindingUtil.inflate(view, R.layout.item_repo, viewGroup, false)
        return RepoItemViewHolder(binding)
    }

    override fun onBindViewHolder(cryptoViewHolder: RepoItemViewHolder, position: Int) {
        cryptoViewHolder.bind(dataSet[position])
    }

    /**
     * method to get size of the recycler view's dataset
     */
    override fun getItemCount(): Int {
        return dataSet.size
    }

    /**
     * This method clears the dataset
     */
    fun clearData() {
        this.dataSet.clear()
    }

    /**
     * This method updates the latest data set to the adapter
     * @param data: List of [Repository] objects
     */
    fun setData(data: List<Repository>) {
        clearData()
        this.dataSet.addAll(data)
    }

    /**
     * ViewHolder class for a single item of repository detail in recycler view
     */
    inner class RepoItemViewHolder(private val binding: ItemRepoBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Method to bind data
         * @param repository repository item containing the details
         */
        fun bind(repository: Repository) {
            with(binding) {
                repoItem = repository
                repository.avatar.let {
                    Picasso.get().load(it).into(avatarView)
                }
                repository.languageColor?.let {
                    (languageIcon.drawable as GradientDrawable).setColor(Color.parseColor(it))
                }

                if (layoutPosition == expandedItemPosition) {
                    expand()
                } else {
                    collapse()
                }

                itemCard.setOnClickListener {
                    if (layoutPosition == expandedItemPosition) {
                        collapse()
                        expandedItemPosition = null
                    } else {
                        val lastExpandedItemPosition = expandedItemPosition
                        expandedItemPosition = layoutPosition
                        notifyItemChanged(layoutPosition)
                        lastExpandedItemPosition?.let { notifyItemChanged(it) }
                    }
                }
            }
        }

        /**
         * expand card
         */
        private fun expand() {
            binding.collapsedGroup.visibility = View.VISIBLE
            if (itemView is CardView) {
                itemView.cardElevation =
                    itemView.resources.getDimensionPixelSize(R.dimen.expanded_card_elevation).toFloat()
            }

        }

        /**
         * collapse card
         */
        private fun collapse() {
            binding.collapsedGroup.visibility = View.GONE
            if (itemView is CardView) {
                itemView.cardElevation = itemView.resources.getDimensionPixelSize(R.dimen.space_zero).toFloat()
            }
        }
    }

}