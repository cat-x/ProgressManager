## Listen the progress of  downloading and uploading in Okhttp (compatible Retrofit and Glide).
add KotlinEx

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
//if you want use OkHttpClient listening,you should call this function in init
OkHttpClient.Builder.useListen()

//if you want use Glide listening,you should call this function in init
Glide.useListen()
```
### second
```kotlin
//you can use this function for OkHttpClient  listen
RequestBuilder<*>.listen(progressListener: ProgressListener): RequestBuilder<*>

//you can use this function for Glide listen
Request.listen(progressListener: ProgressListener): Request
```

### last
if you want to do somthing in UI thread , you can use this function
```java
ProgressManager#runUiThread
```



## ProGuard
```
 -keep class me.jessyan.progressmanager.** { *; }
 -keep interface me.jessyan.progressmanager.** { *; }

 [中文说明](README-zh.md)
```

## The original author
https://github.com/JessYanCoding/ProgressManager