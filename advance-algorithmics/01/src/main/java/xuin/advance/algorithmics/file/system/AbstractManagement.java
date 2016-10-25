package xuin.advance.algorithmics.file.system;

public abstract class AbstractManagement implements IManagement {
    protected StringBuilder data = new StringBuilder();

    public AbstractManagement() {
        data.append(emptyPattern());
    }

    protected abstract String emptyPattern();

    protected int startLocation() {
        return emptyPattern().length();
    }

    protected int root() {
        return Integer.parseInt(data.subSequence(0, startLocation()).toString());
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
