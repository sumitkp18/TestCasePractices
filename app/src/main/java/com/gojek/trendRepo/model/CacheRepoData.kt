package com.gojek.trendRepo.model

import com.gojek.trendRepo.repo.database.generateUUID
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Model class for caching list of trending repositories' details
 */
open class CacheRepoData(
    @PrimaryKey
    var id: String? = generateUUID(),
    var data: RealmList<Repository>? = null,
    var timestamp: Long? = null
) : RealmObject()