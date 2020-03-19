package com.deloitte.testCasePractices.repo.database

import com.deloitte.testCasePractices.model.CacheRepoData
import com.deloitte.testCasePractices.model.Contributor
import com.deloitte.testCasePractices.model.Repository
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList

/**
 * A Realm based database manager with caching
 */
class RealmDatabaseManager : DatabaseManager {

    private var configuration: RealmConfiguration

    companion object {
        const val SCHEMA_VERSION = 1L
        const val DATABASE_NAME = "testCasePractices.realm"
        const val CACHE_EXPIRATION_TIME = 2 * 60 * 60 * 1000
    }

    init {
        configuration = RealmConfiguration.Builder().schemaVersion(SCHEMA_VERSION).name(DATABASE_NAME).build()
        Realm.setDefaultConfiguration(configuration)
    }

    /**
     * deletes repository model objects from DB
     */
    override fun deleteRepoData() {
        getRealmInstance().transaction { realm ->
            val cacheData = findFirst(CacheRepoData::class.java)
            cacheData?.let {
                it.data?.forEach { repository ->
                    repository.builtBy?.forEach { contributor ->
                        realm.deleteById(Contributor::class.java, contributor.id)
                    }
                    realm.deleteById(Repository::class.java, repository.id)
                }
                realm.deleteById(CacheRepoData::class.java, it.id)
            }
        }
    }

    /**
     * fetches saved repository model objects from DB
     */
    override fun getRepoData(): List<Repository>? {
        val cachedData = findFirst(CacheRepoData::class.java)
        return cachedData?.timestamp?.let {
            if (System.currentTimeMillis().minus(it) < CACHE_EXPIRATION_TIME) {
                cachedData.data
            } else {
                null
            }
        }
    }

    /**
     * Inserts new data after deleting existing repository model objects.
     */
    override fun insertRepoData(repoList: List<Repository>) {
        deleteRepoData()
        val realmList = RealmList<Repository>()
        repoList.forEach { realmList.add(it) }
        CacheRepoData(data = realmList, timestamp = System.currentTimeMillis()).save()
    }
}