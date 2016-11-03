package xu.aa.fs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public abstract class AbstractFileSystem<T extends AbstractBlock> implements IFileSystem {
	protected static final String SEPARATOR = "|";
	protected static final char DELETE_INDICATOR = '?';
	protected static final char FRAGMENT = '*';
	protected static final int NOT_EXIST_DELETED_BLOCK = -1;

	protected int rootDeleteBlock = NOT_EXIST_DELETED_BLOCK;

	protected List<T> blocks = new ArrayList<>();

	protected boolean hasDeletedBlock() {
		return rootDeleteBlock != NOT_EXIST_DELETED_BLOCK;
	}

	protected T findBlock(String name) {
	    for (int i = 0; i < blocks.size(); i++) {
			T block = blocks.get(i);
			if (!block.isDeleted() && blockCompare(block.getContent(), name)) {
				block.setIndex(i);
	            return block;
	        }
	    }
	    return null;
	}

	protected boolean exists(String name) {
		return findBlock(name) != null;
	}

	private boolean blockCompare(String block, String name) {
		return StringUtils.remove(block, FRAGMENT).equals(name);
	}
}
