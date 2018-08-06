package me.jessyan.progressmanager.ex.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 上传监听
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestListen {
    String HEADER = "X-RequestListen";
}
