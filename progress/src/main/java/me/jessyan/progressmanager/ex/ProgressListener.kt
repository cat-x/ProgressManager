package me.jessyan.progressmanager.ex

import me.jessyan.progressmanager.ProgressListener
import me.jessyan.progressmanager.body.ProgressInfo
import java.lang.Exception

/**
 * Created by Cat-x on 2018/8/3.
 * For cat-x-ProgressManager
 * Cat-x All Rights Reserved
 */
class ProgressListener : ProgressListener {

    override fun onProgress(progressInfo: ProgressInfo?) {

    }

    override fun onError(id: Long, e: Exception?) {

    }
}