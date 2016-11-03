package xu.aa.fs;

public class VaraiableLengthBlock extends AbstractBlock {
	private int length = 0;

	@Override
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

}
