package com.gojek.trendRepo.model

/**
 * Model class for storing details of a repository
 */
data class Repository(
    val author: String? = null,
    val name: String? = null,
    val avatar: String? = null,
    val url: String? = null,
    val description: String? = null,
    val language: String? = null,
    val languageColor: String? = null,
    val stars: Int? = null,
    val forks: Int? = null,
    val currentPeriodStars: Int? = null,
    val builtBy: List<Contributor>? = null
)

data class Contributor(
    val href: String? = null,
    val avatar: String? = null,
    val username: String? = null
)
