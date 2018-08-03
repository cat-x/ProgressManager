package me.jessyan.progressmanager.ex

import android.net.Uri
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import me.jessyan.progressmanager.ProgressManager
import okhttp3.OkHttpClient
import java.io.InputStream
import me.jessyan.progressmanager.ProgressListener as IProgressListener

/**
 * Created by Cat-x on 2018/8/3.
 * For cat-x-ProgressManager
 * Cat-x All Rights Reserved
 */
const val TAG = "OKHttpListen"

fun RequestBuilder<*>.listen(progressListener: ProgressListener): RequestBuilder<*> {
    var model: Any? = null
    try {
        model = RequestBuilder::class.java.getField("model").also { it.isAccessible = true }.get(this)
    } catch (e: Exception) {
        Log.e(TAG, "Glide model can't get")
        e.printStackTrace()
    }
    when (model) {
        null -> {
            Log.e(TAG, "model(url) is null,can't listen")
        }
        is String -> {
            ProgressManager.getInstance().addResponseListener(model, progressListener)
        }
        is Uri -> {
            if (listOf("http", "https"/*,"ws:","wss:"*/).contains(model.scheme)) {
                ProgressManager.getInstance().addResponseListener(model.toString(), progressListener)
            }
        }
    }
    return this
}

fun Glide.useListen() {
    registry.replace(GlideUrl::class.java, InputStream::class.java,
            OkHttpUrlLoader.Factory(OkHttpClient.Builder().useListen().build()))
}



