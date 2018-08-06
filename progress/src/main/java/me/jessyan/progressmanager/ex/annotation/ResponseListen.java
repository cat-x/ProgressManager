package me.jessyan.progressmanager.ex.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 下载监听
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseListen {

    String HEADER = "X-ResponseListen";
}
