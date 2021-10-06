package com.x.wallpaper

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class MyApplication : Application(){
    companion object{
        @SuppressLint("StaticFieldLeak")//不会造成内存泄漏
        lateinit var context : Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}