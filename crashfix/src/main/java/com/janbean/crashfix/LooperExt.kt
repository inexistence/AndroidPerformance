package com.janbean.crashfix

import android.os.Build
import android.os.Handler
import android.os.Looper
import java.lang.reflect.Constructor

/**
 * Created by huangjianbin on 2022/4/22
 */
fun Looper.asHandler(async: Boolean): Handler {
    // Async support was added in API 16.
    if (!async || Build.VERSION.SDK_INT < 16) {
        return Handler(this)
    }

    if (Build.VERSION.SDK_INT >= 28) {
        return Handler.createAsync(this)
    }

    val constructor: Constructor<Handler>
    try {
        constructor = Handler::class.java.getDeclaredConstructor(
            Looper::class.java,
            Handler.Callback::class.java, Boolean::class.javaPrimitiveType)
    } catch (ignored: NoSuchMethodException) {
        // Hidden constructor absent. Fall back to non-async constructor.
        return Handler(this)
    }
    return constructor.newInstance(this, null, true)
}



fun Looper.isMain(): Boolean {
    return this == Looper.getMainLooper()
}
