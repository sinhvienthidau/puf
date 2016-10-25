package xuin.advance.algorithmics.file.system;

public interface IManagement {
    public static final char DELETE_INDICATOR = '?';

    void add(String element);

    void delete(String element);

    void defragment();
}
