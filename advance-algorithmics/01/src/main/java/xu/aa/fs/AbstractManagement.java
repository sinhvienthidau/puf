package xu.aa.fs;

import xu.aa.gui.Main;

/**
 * Dummy file system.
 * 
 * @author phong.nguyen
 *
 */
public abstract class AbstractManagement {
    protected final static char DELETED_INDICATOR = '-';
    protected final static char VISIBLE_INDICATOR = '+';

    // using a char sequences to make dummy file system.
    // each segment will be mark by an indicator
    // -: dedicate that that segment was deleted
    // +: dedicate that that segment is readable.
    // as dummy char sequence, we follow these understand:
    // each char represent for each sector on hard disk.
    // shifting a sequences called as defragment (same as hard disk defragment)
    //
    // using linked list to maintain the allocation in this dummy file system.
    protected StringBuilder data = new StringBuilder();

    protected Main gui;

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

    public void setGui(Main gui) {
        this.gui = gui;
    }
}
