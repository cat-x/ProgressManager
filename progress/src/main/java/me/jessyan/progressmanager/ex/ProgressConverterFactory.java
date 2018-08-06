package me.jessyan.progressmanager.ex;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.UUID;

import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.ex.annotation.RequestListen;
import me.jessyan.progressmanager.ex.annotation.ResponseListen;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Converter factory that generates uuid for all used progress listeners.
 */
public final class ProgressConverterFactory extends Converter.Factory {


    @Override
    public Converter<ProgressListener, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        for (final Annotation annotation : annotations) {
            if (annotation instanceof RequestListen || annotation instanceof ResponseListen) {
                return new Converter<ProgressListener, String>() {
                    @Override
                    public String convert(ProgressListener value) throws IOException {
                        String key = UUID.randomUUID().toString();
                        ProgressManager manager = ProgressManager.getInstance();
                        if (annotation instanceof RequestListen) {
                            manager.addRequestListener(key, value);
                        } else {
                            manager.addResponseListener(key, value);
                        }
                        return key;
                    }
                };
            }
        }
        return null;
    }
}
