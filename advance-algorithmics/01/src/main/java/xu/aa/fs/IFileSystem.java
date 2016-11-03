package xu.aa.fs;

public interface IFileSystem {
	void add(String content);

	void delete(String content);

	void defrag();
}
