package com.example.imageeditdemo

import android.app.Application
import android.content.Context

/**
 * @author wanglezhi
 * @date   2020/6/11 13:56
 * @discription
 */
class MyApplication:Application() {


    override fun onCreate() {
        super.onCreate()
        GlobalParams.mApplication = this
    }



}