package xu.aa.fs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * variable length file system implementation.
 * 
 * @author phong.nguyen
 *
 */
public class VariableLengthFileSystem extends AbstractFileSystem<VaraiableLengthBlock> {
	private static final int FIXED_LENGHT = 3;
	private int length = 1;
	private Map<Integer, Integer> indexes = new HashMap<>();
	private Map<Integer, Integer> lengths = new HashMap<>();

	@Override
	public void add(String name) {
		if (exists(name)) {
			return;
		}

		VaraiableLengthBlock block = getFirstFitBlock(name);
		if (block != null) {
			block.setContent(StringUtils.rightPad(name, block.getLength(), FRAGMENT));
			block.setDeleted(false);
			rootDeleteBlock = block.getNextDeletedBlock();
		} else {
			block = new VaraiableLengthBlock();
			block.setContent(name);
			block.setLength(name.length());

			indexes.put(blocks.size(), length);
			lengths.put(length, blocks.size());
			blocks.add(block);
			
			length = length + FIXED_LENGHT + block.getLength();
		}
	}

	private VaraiableLengthBlock getFirstFitBlock(String name) {
		int conductor = rootDeleteBlock;
		while (conductor != NOT_EXIST_DELETED_BLOCK) {
			VaraiableLengthBlock block = blocks.get(lengths.get(conductor));
			if (block.getLength() >= name.length()) {
				return block;
			}
		}
		return null;
	}

	@Override
	public void delete(String name) {
		VaraiableLengthBlock block = findBlock(name);
		if (block != null) {
			block.setDeleted(true);
			block.setNextDeletedBlock(rootDeleteBlock);
			rootDeleteBlock = indexes.get(block.getIndex());
		}
	}

	@Override
	public void defrag() {
		rootDeleteBlock = NOT_EXIST_DELETED_BLOCK;
		Iterator<VaraiableLengthBlock> iterator = blocks.iterator();
		while (iterator.hasNext()) {
			VaraiableLengthBlock block = iterator.next();
			if (block.isDeleted()) {
				iterator.remove();
			} else if (block.getContent().contains(String.valueOf(FRAGMENT))) {
				block.setContent(StringUtils.remove(block.getContent(), FRAGMENT));
				block.setLength(block.getContent().length());
			}
		}
	}

	@Override
	public String toString() {
		int bytes = 1;
		StringBuilder builder = new StringBuilder();
		builder.append(rootDeleteBlock).append(SEPARATOR);
		for (VaraiableLengthBlock block : blocks) {
			if (block.isDeleted()) {
				builder.append('{').append(bytes).append('}')
					   .append(DELETE_INDICATOR)
					   .append(block.getNextDeletedBlock())
					   .append('[').append(block.getLength()).append(']')
					   .append(block.getContent());
			} else {
				builder.append('{').append(bytes).append('}')
					   .append('[').append(block.getLength()).append(']').append(block.getContent());
			}
			builder.append(SEPARATOR);

			bytes = bytes + FIXED_LENGHT + block.getLength();
		}

		return builder.toString();
	}
}
