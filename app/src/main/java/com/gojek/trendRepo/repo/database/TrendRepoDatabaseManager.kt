package com.gojek.trendRepo.repo.database

import com.gojek.trendRepo.model.CacheRepoData
import com.gojek.trendRepo.model.Contributor
import com.gojek.trendRepo.model.Repository
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList

/**
 * A Realm based database manager with caching
 */
object TrendRepoDatabaseManager {

    private var configuration: RealmConfiguration
    const val SCHEMA_VERSION = 1L
    const val DATABASE_NAME = "trendRepo.realm"
    const val CACHE_EXPIRATION_TIME = 2 * 60 * 60 * 1000

    init {
        configuration = RealmConfiguration.Builder().schemaVersion(SCHEMA_VERSION).name(DATABASE_NAME).build()
        Realm.setDefaultConfiguration(configuration)
    }

    /**
     * deletes repository model objects from DB
     */
    private fun deleteRepoData() {
        getRealmInstance().transaction { realm ->
            val cacheData = findFirst(CacheRepoData::class.java)
            cacheData?.data?.forEach { repository ->
                repository.builtBy?.forEach { contributor ->
                    realm.deleteById(Contributor::class.java, contributor.id)
                }
                realm.deleteById(Repository::class.java, repository.id)
            }
            realm.deleteById(CacheRepoData::class.java, cacheData?.id)
        }
    }

    /**
     * fetches saved repository model objects from DB
     */
    fun getRepoData(): List<Repository>? {
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
     * Deletes existing repository model objects and inserts new ones.
     */
    fun insertRepoData(repoList: List<Repository>) {
        deleteRepoData()
        val realmList = RealmList<Repository>()
        repoList.forEach { realmList.add(it) }
        CacheRepoData(data = realmList, timestamp = System.currentTimeMillis()).save()
    }
}