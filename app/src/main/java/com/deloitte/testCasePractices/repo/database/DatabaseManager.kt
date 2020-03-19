package com.deloitte.testCasePractices.repo.database

import com.deloitte.testCasePractices.model.Repository

interface DatabaseManager {

    fun deleteRepoData()
    fun getRepoData(): List<Repository>?
    fun insertRepoData(repoList: List<Repository>)
}