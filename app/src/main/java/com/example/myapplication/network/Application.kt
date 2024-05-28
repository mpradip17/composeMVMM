package com.talentwood.network


class Application : android.app.Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private var instance: Application? = null

        @Synchronized
        fun getInstance(): Application? {
            if (instance == null) instance = Application()
            return instance
        }
    }
}