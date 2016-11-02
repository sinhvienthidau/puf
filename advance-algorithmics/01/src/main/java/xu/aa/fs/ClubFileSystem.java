package xu.aa.fs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Management Club.
 * 
 * @author phong.nguyen
 *
 */
public class ClubFileSystem {
	private static final char DELETE_INDICATOR = '?';
	private static final char CLUB_INDICATOR = '*';
	private static final int NOT_EXIST_DELETED_BLOCK = -1;

	private int rootDeleteBlock = NOT_EXIST_DELETED_BLOCK;
	private List<FixedLengthBlock> clubs = new ArrayList<>();

	class FixedLengthBlock {
		boolean deleted = false;
		int nextDeletedBlock = NOT_EXIST_DELETED_BLOCK;
		int rootClubPosition = NOT_EXIST_DELETED_BLOCK;
		String content = null;
		
		// transient field
		int index = 0;
	}

	public void add(String name) {
		if (exists(name) != null) {
			return;
		}

		if (hasDeletedBlock()) {
			FixedLengthBlock block = clubs.get(rootDeleteBlock);
			rootDeleteBlock = block.nextDeletedBlock;
			block.deleted = false;
			block.content = name;
		} else {
			FixedLengthBlock block = new FixedLengthBlock();
			block.content = name;

			clubs.add(block);
		}
	}

	public void delete(String name) {
		FixedLengthBlock block = exists(name);
		if (block != null) {
			block.deleted = true;
			block.nextDeletedBlock = rootDeleteBlock;
			rootDeleteBlock = block.index;
		}
	}

	public void defrag() {
		Iterator<FixedLengthBlock> iterator = clubs.iterator();
		while (iterator.hasNext()) {
			FixedLengthBlock block = iterator.next();
			if (block.deleted) {
				rootDeleteBlock = block.nextDeletedBlock;
				iterator.remove();
			}
		}
	}

	public FixedLengthBlock exists(String name) {
		for (int i = 0; i < clubs.size(); i++) {
			FixedLengthBlock block = clubs.get(i);
			if (!block.deleted && block.content.equals(name)) {
				block.index = i;
				return block;
			}
		}
		return null;
	}

	public boolean hasDeletedBlock() {
		return rootDeleteBlock != -1;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (!clubs.isEmpty()) {
			builder.append(rootDeleteBlock);
			for (FixedLengthBlock block : clubs) {
				if (block.deleted) {
					builder.append(DELETE_INDICATOR);
					builder.append(block.nextDeletedBlock);
					builder.append(block.content);
				} else {
					builder.append(CLUB_INDICATOR);
					builder.append(block.rootClubPosition);
					builder.append(block.content);
				}
			}
		}
		return builder.toString();
	}
}
