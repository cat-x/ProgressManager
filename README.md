## Listen the progress of  downloading and uploading in Okhttp (compatible Retrofit and Glide).
add KotlinEx

[中文说明](README-zh.md)

### Use
``` gradle
//Add it in your root build.gradle at the end of repositories:
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}

//Add the dependency
dependencies {
        implementation 'com.github.cat-x:ProgressManager:v1.0'
}
```

### frist 
```kotlin
//if you want use Glide listening,you should call this function in init
GlideExKt.initGlideUseListen()
//or
Glide.useListen()

//if you want use OkHttpClient listening,you should call this function in init
OkHttpClient.Builder.useListen()

//if you want use Retrofit listening,you should call this function in init
Retrofit retrofit = Retrofit.Builder()
    .client(OkHttpClient.Builder().useListen().build())
    .addConverterFactory(ProgressConverterFactory(pool))
    ...
    .build();
```
### second
```kotlin
//you can use this function for Glide listen
Request.listen(progressListener: ProgressListener): Request

//you can use this function for OkHttpClient listen(Both can, either)
Request<*>.listen(progressListener: ProgressListener): RequestBuilder<*>
RequestBuilder<*>.listen(progressListener: ProgressListener): RequestBuilder<*>
//and For Upload
Request<*>.listen(progressListener: ProgressListener,true): RequestBuilder<*>
RequestBuilder<*>.listen(progressListener: ProgressListener,true): RequestBuilder<*>

//If you use Retrofit listen , you need to add annotations to the method 
public interface Api {

   @GET("/download")//Download ProgressListener
   Observable<Object> download(
   @ResponseListen 
   @Header(ResponseListen.HEADER) ProgressListener listener);

   @POST("/upload")//Upload ProgressListener
   Observable<Object> upload(@Body String id,
   @RequestListen 
   @Header(RequestListen.HEADER) ProgressListener listener);
}

//If you want to is realized in the same Uri addresses for different listening, you can use the following function, make a different Url, but don't worry it won't change the real request address
fun String.makeDiffListen(key: String): String
fun String.makeDiffListen(progressListener: ProgressListener)
fun String.makeDiffListen(progressListener: IProgressListener): String
```

### third
```kotlin
//Remove the old monitoring function, at the same time enable new monitoring function
//you can use this function for Glide listen
Request.reListen(progressListener: ProgressListener): Request

//you can use this function for OkHttpClient  listen(Both can, either)
Request<*>.reListen(progressListener: ProgressListener): RequestBuilder<*>
RequestBuilder<*>.reListen(progressListener: ProgressListener): RequestBuilder<*>
//and For Upload
Request<*>.reListen(progressListener: ProgressListener): RequestBuilder<*>
RequestBuilder<*>.reListen(progressListener: ProgressListener,true): RequestBuilder<*>

//Maybe you just want to delete the current monitoring function
//you can use this function for Glide listen
Request.unListen(progressListener: ProgressListener): Request

//you can use this function for OkHttpClient  listen(Both can, either)
Request<*>.unListen(progressListener: ProgressListener): RequestBuilder<*>
RequestBuilder<*>.unListen(progressListener: ProgressListener): RequestBuilder<*>
//and For Upload
Request<*>.unListen(progressListener: ProgressListener): RequestBuilder<*>
RequestBuilder<*>.unListen(progressListener: ProgressListener,true): RequestBuilder<*>
```

### last
if you want to do somthing in UI thread , you can use this function
```java
ProgressManager#runUiThread
//Or 
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

## The original author
https://github.com/JessYanCoding/ProgressManager   
https://github.com/gumyns/Progress-for-Retrofit-2