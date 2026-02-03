package com.example.imdbmovieapp.repository

import com.example.imdbmovieapp.datasource.LocalDataSource
import com.example.imdbmovieapp.datasource.RemoteDataSource
import com.example.imdbmovieapp.repository.interfaces.ILocalDataSource
import com.example.imdbmovieapp.repository.interfaces.IRemoteDataSource
import javax.inject.Inject

class MovieRepository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
) : IRemoteDataSource by remoteDataSource,
    ILocalDataSource by localDataSource