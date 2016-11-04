package xuin.aa.block;

import java.nio.ByteBuffer;

import org.apache.commons.lang3.StringUtils;

import xuin.aa.utils.ByteBufferUtils;

public class PlayerBlock extends AbstractBlock {
    private int nextClubPlayerIndex = -1;
    private int length;

    private transient int previousIndex = -1;

    public int countBytes() {
        return 1 + 3 * Integer.BYTES + length * Character.BYTES;
    }

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(countBytes());
        ByteBufferUtils.putBoolean(buffer, deleted);
        buffer.putInt(length);
        buffer.putInt(nextDeleteIndex);
        buffer.putInt(nextClubPlayerIndex);
        ByteBufferUtils.putString(buffer, StringUtils.rightPad(data, length, '*'));
        return buffer.array();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('[').append(offset).append(']');
        if (deleted) {
            builder.append('?').append(length).append('/').append(nextDeleteIndex).append('/').append(data);
        } else {
            builder.append(length).append('/').append(nextClubPlayerIndex).append('/').append(data);
        }
        return builder.toString();
    }

    public static PlayerBlock fromBytes(byte[] bytes, int offset) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        PlayerBlock block = new PlayerBlock();
        block.setOffset(offset);
        block.setDeleted(ByteBufferUtils.getBoolean(buffer, 0));
        block.setLength(buffer.getInt(1));
        block.setNextDeleteIndex(buffer.getInt(5));
        block.setNextClubPlayerIndex(buffer.getInt(9));
        block.setData(ByteBufferUtils.getString(buffer, 13, block.getLength()));

        return block;
    }

    public int getNextClubPlayerIndex() {
        return nextClubPlayerIndex;
    }

    public void setNextClubPlayerIndex(int nextClubPlayerIndex) {
        this.nextClubPlayerIndex = nextClubPlayerIndex;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getPreviousIndex() {
        return previousIndex;
    }

    public void setPreviousIndex(int previousIndex) {
        this.previousIndex = previousIndex;
    }

}
