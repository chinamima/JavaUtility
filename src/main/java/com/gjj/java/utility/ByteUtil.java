package com.gjj.java.utility;

/**
 * Created by guojinjun on 2018/08/21.
 */
public class ByteUtil {

    public static String byte2Str(byte[] arrByte) {
        if (arrByte == null || arrByte.length == 0) {
            return null;
        }

        char[] arrDigit = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        char[] arrNew = new char[(arrByte.length * 2)];
        for (int i = 0; i < arrByte.length; i++) {
            byte b = arrByte[i];
            arrNew[(i * 2) + 1] = arrDigit[b & 15];
            arrNew[i * 2] = arrDigit[((byte) (b >>> 4)) & 15];
        }
        return new String(arrNew);
    }
}
