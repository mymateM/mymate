package com.example.mymate

import android.app.Application

class MymateApp: Application() {
    lateinit var dataStoreRepoUser: DataStoreRepoUser

    override fun onCreate() {
        super.onCreate()
        dataStoreRepoUser = DataStoreRepoUser(dataStore)
    }
}