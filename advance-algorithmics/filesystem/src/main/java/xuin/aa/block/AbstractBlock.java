package xuin.aa.block;

public abstract class AbstractBlock {

    protected int offset = 0;
    protected boolean deleted = false;
    protected int nextDeleteIndex = -1;
    protected String data;

    public abstract byte[] toBytes();

    public int getOffset() {
        return offset;
    }
    public void setOffset(int offset) {
        this.offset = offset;
    }
    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public int getNextDeleteIndex() {
        return nextDeleteIndex;
    }
    public void setNextDeleteIndex(int nextDeleteIndex) {
        this.nextDeleteIndex = nextDeleteIndex;
    }

}
