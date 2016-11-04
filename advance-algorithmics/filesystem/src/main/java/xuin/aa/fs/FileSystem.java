package xuin.aa.fs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public abstract class FileSystem {
    public static final int NOT_EXIST_DELETED_BLOCK = -1;

    protected File file = null;

    public FileSystem(String path) throws FileNotFoundException, IOException {
        file = new File(path);
        if (!file.exists()) {
            try (FileOutputStream os = new FileOutputStream(file)) {
                ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
                buffer.putInt(NOT_EXIST_DELETED_BLOCK);
                os.write(buffer.array());
            }
        }
    }

    public int getDeletedRecordRoot() throws FileNotFoundException, IOException {
        byte[] bytes = read(0, 4);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getInt();
    }

    public void updateDeleteRecordRoot(int offset) throws FileNotFoundException, IOException {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(offset);
        write(0, buffer.array());
    }

    public void write(int offset, byte[] bytes) throws FileNotFoundException, IOException {
        try (RandomAccessFile f = new RandomAccessFile(file, "rw")) {
            f.seek(offset);
            f.write(bytes);
        }
    }

    public byte[] read(int offset, int length) throws FileNotFoundException, IOException {
        try (RandomAccessFile f = new RandomAccessFile(file, "rw")) {
            byte[] bytes = new byte[length];
            f.seek(offset);
            f.read(bytes);
            return bytes;
        }
    }

    public void append(byte[] bytes) throws FileNotFoundException, IOException {
        try (FileOutputStream os = new FileOutputStream(file, true)) {
            os.write(bytes);
        }
    }

    public void shift(int from, int to, int length) throws FileNotFoundException, IOException {
        try (RandomAccessFile f = new RandomAccessFile(file, "rw")) {
            f.seek(from);
            byte[] bytes = new byte[length];
            f.read(bytes);

            f.seek(to);
            f.write(bytes);
        }
    }

    public void trunc(int length) throws FileNotFoundException, IOException {
        try (RandomAccessFile f = new RandomAccessFile(file, "rw")) {
            f.setLength(f.length() - length);
        }
    }

    public int getLastOffset() {
        return Long.valueOf(file.length()).intValue();
    }
}
