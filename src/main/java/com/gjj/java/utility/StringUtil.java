package com.gjj.java.utility;

/**
 * Created by guojinjun on 2018/08/31.
 */
public class StringUtil {

    public static String array2String(String[] arr) {
        if (arr == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        sb.append("arrSize:").append(arr.length).append(", join:");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]).append(",");
        }
        return sb.substring(0,sb.length() - 1);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
