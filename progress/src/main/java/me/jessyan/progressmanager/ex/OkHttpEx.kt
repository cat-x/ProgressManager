package me.jessyan.progressmanager.ex

import me.jessyan.progressmanager.ProgressManager
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Created by Cat-x on 2018/8/3.
 * For cat-x-ProgressManager
 * Cat-x All Rights Reserved
 */

fun Request.listen(progressListener: ProgressListener): Request {
    ProgressManager.getInstance().addResponseListener(url().toString(), progressListener)
    return this
}

fun OkHttpClient.Builder.useListen(): OkHttpClient.Builder {
    ProgressManager.getInstance().with(this)
    return this
}