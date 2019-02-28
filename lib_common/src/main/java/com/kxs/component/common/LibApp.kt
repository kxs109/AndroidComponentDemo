package com.kxs.component.common

import android.app.Application
import android.content.Context
import kotlin.properties.Delegates

/**
 * Created by zhh on 2019/2/13.
 */
open class LibApp : Application() {
    companion object {
        var context: Context by Delegates.notNull()
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}