package xuin.aa.block;

import java.nio.ByteBuffer;

import xuin.aa.utils.ByteBufferUtils;

public class ClubBlock extends AbstractBlock {
    // to write this block to file needs
    // 1 byte for delete indicator
    // 4 bytes for storing next delete index
    // 4 bytes for storing root player index
    // 6 bytes for storing club name (3 characters)
    public static final int BYTES = 1 + Integer.BYTES + Integer.BYTES + 6;

    private int rootPlayerIndex = -1;

    public int getRootPlayerIndex() {
        return rootPlayerIndex;
    }

    public void setRootPlayerIndex(int rootPlayerIndex) {
        this.rootPlayerIndex = rootPlayerIndex;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('[').append(offset).append(']');
        if (deleted) {
            builder.append('?').append(nextDeleteIndex).append('/').append(data);
        } else {
            builder.append(data).append('/').append(rootPlayerIndex);
        }
        return builder.toString();
    }

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(BYTES);
        ByteBufferUtils.putBoolean(buffer, deleted);
        buffer.putInt(nextDeleteIndex);
        buffer.putInt(rootPlayerIndex);
        ByteBufferUtils.putString(buffer, data);

        return buffer.array();
    }

    public static ClubBlock fromBytes(byte[] bytes, int offset) {
        ClubBlock block = new ClubBlock();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        block.setDeleted(ByteBufferUtils.getBoolean(buffer, 0));
        block.setNextDeleteIndex(buffer.getInt(1));
        block.setRootPlayerIndex(buffer.getInt(5));
        block.setData(ByteBufferUtils.getString(buffer, 9, 3));

        block.setOffset(offset);

        return block;
    }
}
