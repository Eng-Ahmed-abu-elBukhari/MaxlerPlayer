package com.maxler.roaa.data.helper

import androidx.multidex.MultiDexApplication

class App: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }



    companion object{
        private var instance:App? = null

        fun getContext():App{
            return instance!!
        }
    }


}