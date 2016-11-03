package xu.aa.fs;

interface IBlock {

	boolean isDeleted();

	int getNextDeletedBlock();

	String getContent();

	int getLength();

}