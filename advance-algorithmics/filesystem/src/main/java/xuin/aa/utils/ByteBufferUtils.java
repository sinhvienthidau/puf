package xuin.aa.utils;

import java.nio.ByteBuffer;

public final class ByteBufferUtils {
    public static final byte TRUE = (byte) 1;
    public static final byte FALSE = (byte) 0;

    public static String getString(ByteBuffer buffer, int offset, int length) {
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = buffer.getChar(offset + i * Character.BYTES);
        }
        return new String(chars);
    }

    public static void putString(ByteBuffer buffer, String s) {
        for (Character c : s.toCharArray()) {
            buffer.putChar(c);
        }
    }

    public static boolean getBoolean(ByteBuffer buffer, int offset) {
        return buffer.get(0) == TRUE;
    }

    public static void putBoolean(ByteBuffer buffer, boolean b) {
        buffer.put(b ? TRUE : FALSE);
    }
}
