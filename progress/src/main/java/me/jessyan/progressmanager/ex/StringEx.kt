package me.jessyan.progressmanager.ex

import android.os.SystemClock
import me.jessyan.progressmanager.ProgressManager
import me.jessyan.progressmanager.ProgressListener as IProgressListener

/**
 * Created by Cat-x on 2018/8/6.
 * For cat-x-ProgressManager
 * Cat-x All Rights Reserved
 */
fun String.makeDiffListen(key: String): String {
    return this + ProgressManager.IDENTIFICATION_NUMBER + key
}

fun String.makeDiffListen(progressListener: IProgressListener): String {
    return this + ProgressManager.IDENTIFICATION_NUMBER + SystemClock.elapsedRealtime() + progressListener.hashCode() + ProgressManager.SUFFIX_RANDOM
}

fun String.makeDiffListen(progressListener: ProgressListener): String {
    return this + ProgressManager.IDENTIFICATION_NUMBER + SystemClock.elapsedRealtime() + progressListener.hashCode() + ProgressManager.SUFFIX_RANDOM
}