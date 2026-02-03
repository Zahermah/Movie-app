package com.example.imdbmovieapp.repository.interfaces

import kotlinx.coroutines.flow.Flow

interface INetworkObserver {
    val isConnected: Flow<Boolean>
}