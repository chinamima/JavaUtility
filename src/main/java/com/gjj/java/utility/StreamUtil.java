package com.gjj.java.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by guojinjun on 2018/08/13.
 */
public class StreamUtil {

    public static final int BUFFER_SIZE = 4096;

    public static boolean copy(InputStream is, OutputStream os) {
        final byte[] buffer = new byte[BUFFER_SIZE];
        try {
            int count;
            while ((count = is.read(buffer)) != -1) {
                os.write(buffer, 0, count);
            }
            os.flush();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public static String inputStreamToString(InputStream is) {
        StringBuffer sb = new StringBuffer();
        try {
            String line = "";
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "utf-8"));
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
