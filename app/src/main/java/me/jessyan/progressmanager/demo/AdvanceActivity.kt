/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.jessyan.progressmanager.demo

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import me.jessyan.progressmanager.ProgressListener
import me.jessyan.progressmanager.ProgressManager
import me.jessyan.progressmanager.body.ProgressInfo
import me.jessyan.progressmanager.ex.initGlideUseListen
import me.jessyan.progressmanager.ex.listen
import me.jessyan.progressmanager.ex.makeDiffListen
import me.jessyan.progressmanager.ex.useListen
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.*

/**
 * ================================================
 * 这里为了展示本框架的高级功能,使用同一个 url 地址根据 Post 请求参数的不同而下载或上传不同的资源
 *
 * @see {@link .initListener}
 * Created by JessYan on 08/06/2017 12:59
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
class AdvanceActivity : AppCompatActivity(), View.OnClickListener {
    private var mImageView: ImageView? = null
    private var mOkHttpClient: OkHttpClient? = OkHttpClient.Builder().useListen().build()
    private var mGlideProgress: ProgressBar? = null
    private var mDownloadProgress: ProgressBar? = null
    private var mUploadProgress: ProgressBar? = null
    private var mGlideProgressText: TextView? = null
    private var mDownloadProgressText: TextView? = null
    private var mUploadProgressText: TextView? = null

    private var mLastDownloadingInfo: ProgressInfo? = null
    private var mLastUploadingingInfo: ProgressInfo? = null
    private var mHandler: Handler? = null
    private var mNewImageUrl: String = "http://jessyancoding.github.io/images/RxCache.png".makeDiffListen(glideListener)
    private var mNewDownloadUrl: String = "http://jessyancoding.github.io/images/RxCache.png".makeDiffListen(downloadListener)
    private var mNewUploadUrl: String = "http://upload.qiniu.com/?JessYan=test"


    private//说明已经加载完成
    val glideListener: ProgressListener
        get() = object : ProgressListener {
            override fun useUIBack(): Boolean {
                return true
            }

            override fun onProgress(progressInfo: ProgressInfo) {
                val progress = progressInfo.percent
                mGlideProgress!!.progress = progress
                mGlideProgressText!!.text = progress.toString() + "%"
                Log.d(TAG, "--Glide-- " + progress + " %  " + progressInfo.speed + " byte/s  " + progressInfo.toString())
                if (progressInfo.isFinish) {
                    Log.d(TAG, "--Glide-- finish")
                }
            }

            override fun onError(id: Long, e: Exception) {
                mHandler!!.post {
                    mGlideProgress!!.progress = 0
                    mGlideProgressText!!.text = "error"
                }
            }
        }

    private// 如果你不屏蔽用户重复点击上传或下载按钮,就可能存在同一个 Url 地址,上一次的上传或下载操作都还没结束,
    // 又开始了新的上传或下载操作,那现在就需要用到 id(请求开始时的时间) 来区分正在执行的进度信息
    // 这里我就取最新的上传进度用来展示,顺便展示下 id 的用法
    //因为是以请求开始时的时间作为 Id ,所以值越大,说明该请求越新
    //说明已经上传完成
    val uploadListener: ProgressListener
        get() = object : ProgressListener {
            override fun useUIBack(): Boolean {
                return true
            }

            override fun onProgress(progressInfo: ProgressInfo) {
                if (mLastUploadingingInfo == null) {
                    mLastUploadingingInfo = progressInfo
                }
                if (progressInfo.id < mLastUploadingingInfo!!.id) {
                    return
                } else if (progressInfo.id > mLastUploadingingInfo!!.id) {
                    mLastUploadingingInfo = progressInfo
                }

                val progress = mLastUploadingingInfo!!.percent
                mUploadProgress!!.progress = progress
                mUploadProgressText!!.text = progress.toString() + "%"
                Log.d(TAG, "--Upload-- " + progress + " %  " + mLastUploadingingInfo!!.speed + " byte/s  " + mLastUploadingingInfo!!.toString())
                if (mLastUploadingingInfo!!.isFinish) {
                    Log.d(TAG, "--Upload-- finish")
                }
            }

            override fun onError(id: Long, e: Exception) {
                mHandler!!.post {
                    mUploadProgress!!.progress = 0
                    mUploadProgressText!!.text = "error"
                }
            }
        }

    private// 如果你不屏蔽用户重复点击上传或下载按钮,就可能存在同一个 Url 地址,上一次的上传或下载操作都还没结束,
    // 又开始了新的上传或下载操作,那现在就需要用到 id(请求开始时的时间) 来区分正在执行的进度信息
    // 这里我就取最新的下载进度用来展示,顺便展示下 id 的用法
    //因为是以请求开始时的时间作为 Id ,所以值越大,说明该请求越新
    //说明已经下载完成
    val downloadListener: ProgressListener
        get() = object : ProgressListener {
            override fun useUIBack(): Boolean {
                return true
            }

            override fun onProgress(progressInfo: ProgressInfo) {

                if (mLastDownloadingInfo == null) {
                    mLastDownloadingInfo = progressInfo
                }
                if (progressInfo.id < mLastDownloadingInfo!!.id) {
                    return
                } else if (progressInfo.id > mLastDownloadingInfo!!.id) {
                    mLastDownloadingInfo = progressInfo
                }

                val progress = mLastDownloadingInfo!!.percent
                mDownloadProgress!!.progress = progress
                mDownloadProgressText!!.text = progress.toString() + "%"
                Log.d(TAG, "--Download-- " + progress + " %  " + mLastDownloadingInfo!!.speed + " byte/s  " + mLastDownloadingInfo!!.toString())
                if (mLastDownloadingInfo!!.isFinish) {
                    Log.d(TAG, "--Download-- finish")
                }
            }

            override fun onError(id: Long, e: Exception) {
                mHandler!!.post {
                    mDownloadProgress!!.progress = 0
                    mDownloadProgressText!!.text = "error"
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHandler = Handler()
        initView()
        initListener()
        //在 Activity 中显示进度条的同时,也在 Fragment 中显示对应 url 的进度条,为了展示此框架的多端同步更新某一个进度信息
        supportFragmentManager.beginTransaction().add(R.id.fragment_container,
                AdvanceFragment.newInstance(mNewImageUrl, mNewDownloadUrl, mNewUploadUrl)).commit()
    }


    private fun initView() {
        setContentView(R.layout.activity_advance)
        mImageView = findViewById(R.id.imageView)
        mGlideProgress = findViewById(R.id.glide_progress)
        mDownloadProgress = findViewById(R.id.download_progress)
        mUploadProgress = findViewById(R.id.upload_progress)
        mGlideProgressText = findViewById(R.id.glide_progress_text)
        mDownloadProgressText = findViewById(R.id.download_progress_text)
        mUploadProgressText = findViewById(R.id.upload_progress_text)
        findViewById<View>(R.id.glide_start).setOnClickListener(this)
        findViewById<View>(R.id.download_start).setOnClickListener(this)
        findViewById<View>(R.id.upload_start).setOnClickListener(this)
    }

    private fun initListener() {
        //图片和下载 (上传也同样支持) 使用同一个 url 地址,是为了展示高级功能
        //高级功能是为了应对当需要使用同一个 url 地址根据 Post 请求参数的不同而下载或上传不同资源的情况
        //"http://jessyancoding.github.io/images/RxCache.png" 会重定向到 "http://jessyan.me/images/RxCache.png"
        //所以也展示了高级功能同时完美兼容重定向
        //这里需要注意的是虽然使用的是新的 url 地址进行上传或下载,但实际请求服务器的 url 地址,还是原始的 url 地址
        //在监听器内部已经进行了处理,所以高级功能并不会影响服务器的请求
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.glide_start -> glideStart()
            R.id.download_start -> downloadStart()
            R.id.upload_start -> uploadStart()
        }
    }

    /**
     * 点击开始上传资源,为了演示,就不做重复点击的处理,即允许用户在还有进度没完成的情况下,使用同一个 url 开始新的上传
     */
    private fun uploadStart() {
        Thread(Runnable {
            try {
                //为了方便就不动态申请权限了,直接将文件放到CacheDir()中
                val file = File(cacheDir, "a.java")
                //读取Assets里面的数据,作为上传源数据
                writeToFile(assets.open("a.java"), file)

                val request = Request.Builder()
                        .url(mNewUploadUrl!!)
                        //Okhttp/Retofit 上传监听
                        .listen(object : me.jessyan.progressmanager.ex.ProgressListener() {
                            override fun useUIBack(): Boolean {
                                return true
                            }

                            override fun onProgress(progressInfo: ProgressInfo?) {
                                uploadListener.onProgress(progressInfo)
                            }

                            override fun onError(id: Long, e: java.lang.Exception?) {
                                uploadListener.onError(id, e)
                            }
                        }, true)
                        .post(RequestBody.create(MediaType.parse("multipart/form-data"), file))
                        .build()

                val response = mOkHttpClient!!.newCall(request).execute()
                response.body()
            } catch (e: IOException) {
                e.printStackTrace()
                //当外部发生错误时,使用此方法可以通知所有监听器的 onError 方法
                ProgressManager.getInstance().notifyOnErorr(mNewUploadUrl, e)
            }
        }).start()
    }

    /**
     * 点击开始下载资源,为了演示,就不做重复点击的处理,即允许用户在还有进度没完成的情况下,使用同一个 url 开始新的下载
     */
    private fun downloadStart() {
        Thread(Runnable {
            try {
                val request = Request.Builder()
                        .url(mNewDownloadUrl!!)
                        //Okhttp/Retofit 下载监听
                        .listen(object : me.jessyan.progressmanager.ex.ProgressListener() {
                            override fun useUIBack(): Boolean {
                                return true
                            }

                            override fun onProgress(progressInfo: ProgressInfo?) {
                                downloadListener.onProgress(progressInfo)
                            }

                            override fun onError(id: Long, e: java.lang.Exception?) {
                                downloadListener.onError(id, e)
                            }
                        })
                        .build()

                val response = mOkHttpClient!!.newCall(request).execute()

                val `is` = response.body()!!.byteStream()
                //为了方便就不动态申请权限了,直接将文件放到CacheDir()中
                val file = File(cacheDir, "download")
                val fos = FileOutputStream(file)
                val bis = BufferedInputStream(`is`)
                val buffer = ByteArray(1024)
                var len: Int = 0
                while ((bis.read(buffer).also { len = it }) != -1) {
                    fos.write(buffer, 0, len)
                }
                fos.flush()
                fos.close()
                bis.close()
                `is`.close()


            } catch (e: IOException) {
                e.printStackTrace()
                //当外部发生错误时,使用此方法可以通知所有监听器的 onError 方法
                ProgressManager.getInstance().notifyOnErorr(mNewDownloadUrl, e)
            }
        }).start()
    }

    /**
     * 点击开始 Glide 加载图片,为了演示,就不做重复点击的处理,但是 Glide 自己对重复加载做了处理
     * 即重复加载同一个 Url 时,停止还在请求当中的进度,再开启新的加载
     */
    private fun glideStart() {
        initGlideUseListen(this)
        Glide.with(this)
                .load(mNewImageUrl)
                .listen(object : me.jessyan.progressmanager.ex.ProgressListener() {
                    override fun useUIBack(): Boolean {
                        return true
                    }

                    override fun onProgress(progressInfo: ProgressInfo?) {
                        glideListener.onProgress(progressInfo)
                    }

                    override fun onError(id: Long, e: java.lang.Exception?) {
                        glideListener.onError(id, e)
                    }
                })
                .apply(RequestOptions.centerCropTransform()
                        .placeholder(R.color.colorPrimary)
                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(mImageView!!)
    }


    companion object {
        private val TAG = "AdvanceActivity"

        @Throws(IOException::class)
        fun writeToFile(`in`: InputStream, file: File): File {
            val out = FileOutputStream(file)
            val buf = ByteArray(1024)
            var num = 0
            while ((`in`.read(buf).also { num = it }) != -1) {
                out.write(buf, 0, buf.size)
            }
            out.close()
            return file
        }
    }
}
