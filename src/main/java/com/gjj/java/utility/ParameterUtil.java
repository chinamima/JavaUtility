package com.gjj.java.utility;

/**
 * Created by guojinjun on 2018/04/02.
 */
public class ParameterUtil {

    public static <T> T getArgument(Object obj, Class<T> clazz) {
        return obj == null ? null : (T) obj;
    }

}
