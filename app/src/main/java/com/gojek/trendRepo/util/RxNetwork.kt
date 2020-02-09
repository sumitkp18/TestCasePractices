package com.gojek.trendRepo.util

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.InternetObservingSettings
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.strategy.SocketInternetObservingStrategy
import io.reactivex.Observable
import io.reactivex.Single

/**
 * RxNetwork is a utility class for listening network connection state with RxJava Observables.
 */
class RxNetwork {

    companion object {

        const val TAG = "RxNetwork"
        const val DEFAULT_HOST = "www.google.com"
        const val NETWORK_NOT_AVAILABLE = "NetworkNotAvailable"

        /**
         * Internet Observing Settings -  Contains state of internet connectivity settings.
         * We should use its Builder for creating new settings
         */
        private fun getInternetObservingSettings(host: String? = null): InternetObservingSettings {
            return InternetObservingSettings.builder()
                    .host(host ?: DEFAULT_HOST)
                    .strategy(SocketInternetObservingStrategy())
                    .build()
        }

        /**
         * Checks connectivity with the Internet. This operation is performed only once.
         *
         * @return RxJava Single with Boolean - true, when we have connection with host and false if
         * not
         */
        fun isInternetAvailable(host: String? = null): Single<Boolean> {
            return ReactiveNetwork.checkInternetConnectivity(getInternetObservingSettings(host))
                    .subscribeOn(AppRxSchedulers.network())
        }

        /**
         * Returns as Observable which emits Network not available error.
         * */
        fun <T> getNetworkNotAvailable(message: String? = null, type: T): Observable<T> {
            return Observable.create<T> { subscriber ->
                subscriber.onError(Throwable(message ?: NETWORK_NOT_AVAILABLE))
                subscriber.onComplete()
            }
        }
    }
}