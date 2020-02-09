package com.gojek.trendRepo.util

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Global Schedulers for the whole application.
 */
object AppRxSchedulers {

    private val database: Scheduler = Schedulers.single()
    private val network: Scheduler = Schedulers.io()
    private val mainThread: Scheduler = AndroidSchedulers.mainThread()

    /**
     * Returns networkIO executor with a threadpool of three threads
     */
    fun network(): Scheduler = network

    /**
     * Returns Database executor with a threadpool of three threads
     */
    fun database(): Scheduler = database

    /**
     * Returns mainThreadExecutor
     */
    fun mainThread(): Scheduler = mainThread

}