package xu.aa.fs;

public abstract class AbstractBlock implements IBlock {
	private boolean deleted = false;
	private int nextDeletedBlock = -1;
	private String content = null;
	private transient int index = 0;

	@Override
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public int getNextDeletedBlock() {
		return nextDeletedBlock;
	}

	public void setNextDeletedBlock(int nextDeletedBlock) {
		this.nextDeletedBlock = nextDeletedBlock;
	}

	@Override
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}