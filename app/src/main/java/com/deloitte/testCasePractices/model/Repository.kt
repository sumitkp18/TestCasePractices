package com.deloitte.testCasePractices.model

import com.deloitte.testCasePractices.repo.database.generateUUID
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Model class for storing details of a repository
 */
open class Repository(
    @PrimaryKey
    var id: String = generateUUID(),
    var author: String? = null,
    var name: String? = null,
    var avatar: String? = null,
    var url: String? = null,
    var description: String? = null,
    var language: String? = null,
    var languageColor: String? = null,
    var stars: Int? = null,
    var forks: Int? = null,
    var currentPeriodStars: Int? = null,
    var builtBy: RealmList<Contributor>? = null
) : RealmObject()

open class Contributor(
    @PrimaryKey
    var id: String? = generateUUID(),
    var href: String? = null,
    var avatar: String? = null,
    var username: String? = null
) : RealmObject()
