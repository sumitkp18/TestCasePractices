package com.deloitte.testCasePractices

import android.app.Application
import io.realm.Realm
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * The Base application class for the app
 * It initialises the koin Modules involved in the Project
 */
class TestCasePracticesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        startKoin {
            androidLogger()
            androidContext(this@TestCasePracticesApplication)
            modules(listOf(networkModule, repoModule, fetchVMModule))
        }

    }
}