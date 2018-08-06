package me.jessyan.progressmanager.ex

import android.util.Log
import me.jessyan.progressmanager.ProgressManager
import okhttp3.OkHttpClient
import okhttp3.Request
import me.jessyan.progressmanager.ProgressListener as IProgressListener

/**
 * Created by Cat-x on 2018/8/3.
 * For cat-x-ProgressManager
 * Cat-x All Rights Reserved
 */
const val TAG2 = "OKHttpListen"

fun Request.reListen(progressListener: ProgressListener, deleteProgressListener: ProgressListener? = null, isUpload: Boolean = false): Request {
    return unListen(deleteProgressListener, isUpload).listen(progressListener, isUpload)
}

fun Request.unListen(progressListener: ProgressListener? = null, isUpload: Boolean = false): Request {
    when {
        isUpload && progressListener == null -> {
            ProgressManager.getInstance().deleteRequestListener(url().toString())
        }
        isUpload && progressListener != null -> {
            ProgressManager.getInstance().deleteRequestListener(url().toString(), progressListener)
        }
        !isUpload && progressListener == null -> {
            ProgressManager.getInstance().deleteResponseListener(url().toString())
        }
        !isUpload && progressListener != null -> {
            ProgressManager.getInstance().deleteResponseListener(url().toString(), progressListener)
        }
    }
    return this
}

fun Request.listen(progressListener: ProgressListener, isUpload: Boolean = false): Request {
    if (isUpload) {
        ProgressManager.getInstance().addRequestListener(url().toString(), progressListener)
    } else {
        ProgressManager.getInstance().addResponseListener(url().toString(), progressListener)
    }

    return this
}

fun Request.Builder.reListen(progressListener: ProgressListener, deleteProgressListener: ProgressListener? = null, isUpload: Boolean = false): Request.Builder {
    return unListen(deleteProgressListener, isUpload).listen(progressListener, isUpload)
}


fun Request.Builder.unListen(progressListener: ProgressListener? = null, isUpload: Boolean = false): Request.Builder {
    var url: Any? = null
    try {
        url = Request.Builder::class.java.getField("url").also { it.isAccessible = true }.get(this)
    } catch (e: Exception) {
        Log.e(TAG2, "Request.Builder url can't get")
        e.printStackTrace()
    }
    if (url != null) {
        when {
            isUpload && progressListener == null -> {
                ProgressManager.getInstance().deleteRequestListener(url.toString())
            }
            isUpload && progressListener != null -> {
                ProgressManager.getInstance().deleteRequestListener(url.toString(), progressListener)
            }
            !isUpload && progressListener == null -> {
                ProgressManager.getInstance().deleteResponseListener(url.toString())
            }
            !isUpload && progressListener != null -> {
                ProgressManager.getInstance().deleteResponseListener(url.toString(), progressListener)
            }
        }
    } else {
        Log.e(TAG2, "Request.Builder url is null,can't unListen")
    }

    return this
}

fun Request.Builder.listen(progressListener: ProgressListener, isUpload: Boolean = false): Request.Builder {
    var url: Any? = null
    try {
        url = Request.Builder::class.java.getDeclaredField("url").also { it.isAccessible = true }.get(this)
    } catch (e: Exception) {
        Log.e(TAG2, "Request.Builder url can't get")
        e.printStackTrace()
    }
    if (url != null) {
        if (isUpload) {
            ProgressManager.getInstance().addRequestListener(url.toString(), progressListener)
        } else {
            ProgressManager.getInstance().addResponseListener(url.toString(), progressListener)
        }
    } else {
        Log.e(TAG2, "Request.Builder url is null,can't listen")
    }
    return this
}

fun OkHttpClient.Builder.useListen(): OkHttpClient.Builder {
    ProgressManager.getInstance().with(this)
    return this
}


//var listenerMap:Triple<RequestBody, String,IProgressListener>