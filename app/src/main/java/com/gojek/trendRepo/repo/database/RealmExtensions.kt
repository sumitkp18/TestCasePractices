package com.gojek.trendRepo.repo.database

import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.kotlin.deleteFromRealm
import java.util.UUID

/**
 * Query to the database with RealmQuery instance as argument. Return first result, or null.
 */
fun <T : RealmModel> findFirst(clazz: Class<T>): T? {
    getRealmInstance().use { realm ->
        val item: T? = realm.where(clazz).findFirst()
        return if (item != null && RealmObject.isValid(item)) realm.copyFromRealm(item) else null
    }
}

/**
 * Delete an object from the database by id. Make sure to call this function from inside
 * transaction.
 * @param id the id of the object
 */
fun <T : RealmModel> Realm.deleteById(clazz: Class<T>, id: String?, idField: String = "id") {
    where(clazz).equalTo(idField, id).findFirst()?.deleteFromRealm()
}

/**
 * Creates a new entry in database or updates an existing one. If entity has no primary key, always create a new one.
 * If has primary key, it tries to updates an existing one.
 */
fun <T : RealmModel> T.save() {
    getRealmInstance().transaction { realm ->
        if (this.hasPrimaryKey(realm)) realm.copyToRealmOrUpdate(this) else realm.copyToRealm(this)
    }
}

/**
 * Utility extension for modifying database. Create a transaction, run the function passed as argument,
 * commit transaction and close realm instance.
 */
fun Realm.transaction(action: (Realm) -> Unit) {
    use { executeTransaction { action(this) } }
}

fun <T : RealmModel> T.hasPrimaryKey(realm: Realm): Boolean {
    realm.schema.get(this.javaClass.simpleName)?.let {
        throw IllegalArgumentException(
            this.javaClass.simpleName + " is not part of the schema for this Realm. " +
                    "Did you add realm-android plugin in your build.gradle file?"
        )
    }
    return realm.schema.get(this.javaClass.simpleName)?.hasPrimaryKey() == true
}

fun getRealmInstance(): Realm {
    return Realm.getDefaultInstance()
}

fun <T : RealmModel> T.getRealmInstance(): Realm {
    return Realm.getDefaultInstance()
}

/**
 * generates UUID
 */
fun generateUUID(): String = UUID.randomUUID().toString()