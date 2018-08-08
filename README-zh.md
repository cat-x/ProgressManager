## 监听在 Okhttp、 Retrofit 、 Glide上传和下载进度变化
add KotlinEx

[英文说明](README.md)

### Use
``` gradle
//添加一下到你的根目录下的 build.gradle的repositories中:
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}

//添加这个依赖
dependencies {
        implementation 'com.github.cat-x:ProgressManager:v1.2'
}
```

### 第一步 
```kotlin
//使用以下函数在Glide中初始化
GlideExKt.initGlideUseListen()
//或者
Glide.useListen()

//使用以下函数在OkHttpClient中初始化
OkHttpClient.Builder.useListen()

//使用以下函数在Retrofit中初始化
Retrofit retrofit = Retrofit.Builder()
    .client(OkHttpClient.Builder().useListen().build())
    .addConverterFactory(ProgressConverterFactory(pool))
    ...
    .build();
```
### 第二步
```kotlin
//调用下面的函数即可实现Glide进度监听
Request.listen(progressListener: ProgressListener): Request

//调用下面的函数即可实现OkHttpClient进度监听(二选一)
Request<*>.listen(progressListener: ProgressListener): RequestBuilder<*>
RequestBuilder<*>.listen(progressListener: ProgressListener): RequestBuilder<*>
//上传监听
Request<*>.listen(progressListener: ProgressListener,true): RequestBuilder<*>
RequestBuilder<*>.listen(progressListener: ProgressListener,true): RequestBuilder<*>

//调用下面的函数即可实现Retrofit进度监听
public interface Api {

   @GET("/download")//下载监听
   Observable<Object> download(
   @ResponseListen 
   @Header(ResponseListen.HEADER) ProgressListener listener);

   @POST("/upload")//上传监听
   Observable<Object> upload(@Body String id,
   @RequestListen 
   @Header(RequestListen.HEADER) ProgressListener listener);
}

//如果你想在同一个Uri地址中实现多个不同的监听，你可以使用以下函数，制作不同的Url地址，但不用担心它不会更改真正的请求地址
fun String.makeDiffListen(key: String): String
fun String.makeDiffListen(progressListener: ProgressListener)
fun String.makeDiffListen(progressListener: IProgressListener): String
```

### 第三部
```kotlin
//移除旧的监听，同时使用新的监听
//调用下面的函数即可实现Glide进度监听
Request.reListen(progressListener: ProgressListener): Request

//调用下面的函数即可实现OkHttpClient进度监听(二选一)
Request<*>.reListen(progressListener: ProgressListener): RequestBuilder<*>
RequestBuilder<*>.reListen(progressListener: ProgressListener): RequestBuilder<*>
//上传监听
Request<*>.reListen(progressListener: ProgressListener,true): RequestBuilder<*>
RequestBuilder<*>.reListen(progressListener: ProgressListener,true): RequestBuilder<*>

//你也可以只是移除监听
//对于Glide
Request.unListen(progressListener: ProgressListener): Request

//对于GlideOkHttpClient
Request<*>.unListen(progressListener: ProgressListener): RequestBuilder<*>
RequestBuilder<*>.unListen(progressListener: ProgressListener): RequestBuilder<*>
//上传的
Request<*>.reListen(progressListener: ProgressListener): RequestBuilder<*>
RequestBuilder<*>.unListen(progressListener: ProgressListener,true): RequestBuilder<*>
```

### 最后
如果你想在UI线程中进行回调操作
```java
ProgressManager#runUiThread
//或者 
open class ProgressListener : ProgressListener {
    override fun useUIBack(): Boolean {
        return true
    }
}
```

## ProGuard
```
 -keep class me.jessyan.progressmanager.** { *; }
 -keep interface me.jessyan.progressmanager.** { *; }
```

## 原作者
https://github.com/JessYanCoding/ProgressManager   
https://github.com/gumyns/Progress-for-Retrofit-2