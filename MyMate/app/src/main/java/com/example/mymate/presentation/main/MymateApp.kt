package com.example.mymate.presentation.main

import android.app.Application
import com.example.mymate.DataStoreRepoUser
import com.example.mymate.dataStore
import com.example.mymate.util.FontManager

class MymateApp: Application() {
    lateinit var dataStoreRepoUser: DataStoreRepoUser

    override fun onCreate() {
        super.onCreate()
        dataStoreRepoUser = DataStoreRepoUser(dataStore)

        FontManager.init(this)
    }
}