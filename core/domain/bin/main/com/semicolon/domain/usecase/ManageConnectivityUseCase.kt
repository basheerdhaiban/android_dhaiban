package com.semicolon.domain.usecase

import com.semicolon.domain.repository.NetworkConnectivity

class ManageConnectivityUseCase(private val networkConnectivity: NetworkConnectivity) {
    fun getNetworkStatus() = networkConnectivity.observe()
    fun isConnected(): Boolean {
        println("isConnected${networkConnectivity.isNetworkAvailable()}")
        return networkConnectivity.isNetworkAvailable()
    }
}