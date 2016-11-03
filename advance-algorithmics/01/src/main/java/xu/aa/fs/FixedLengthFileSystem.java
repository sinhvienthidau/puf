package xu.aa.fs;

import java.util.Iterator;

/**
 * fixed length file system implementation.
 * 
 * @author phong.nguyen
 *
 */
public class FixedLengthFileSystem extends AbstractFileSystem<FixedLengthBlock> {
	@Override
	public void add(String name) {
		if (exists(name)) {
            return;
        }

        if (hasDeletedBlock()) {
            FixedLengthBlock block = blocks.get(rootDeleteBlock);
			rootDeleteBlock = block.getNextDeletedBlock();
			block.setDeleted(false);
			block.setContent(name);
        } else {
            FixedLengthBlock block = new FixedLengthBlock();
			block.setContent(name);

            blocks.add(block);
        }
    }

    @Override
	public void delete(String name) {
        FixedLengthBlock block = findBlock(name);
        if (block != null) {
			block.setDeleted(true);
			block.setNextDeletedBlock(rootDeleteBlock);
			rootDeleteBlock = block.getIndex();
        }
    }

    @Override
	public void defrag() {
        Iterator<FixedLengthBlock> iterator = blocks.iterator();
        while (iterator.hasNext()) {
            FixedLengthBlock block = iterator.next();
			if (block.isDeleted()) {
				rootDeleteBlock = block.getNextDeletedBlock();
                iterator.remove();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (!blocks.isEmpty()) {
            builder.append(rootDeleteBlock).append(SEPARATOR);
            for (FixedLengthBlock block : blocks) {
				if (block.isDeleted()) {
                    builder.append(DELETE_INDICATOR);
					builder.append(block.getNextDeletedBlock());
					builder.append(FRAGMENT);
                } else {
					builder.append(block.getContent());
                }
                builder.append(SEPARATOR);
            }
        }
        return builder.toString();
    }
}
