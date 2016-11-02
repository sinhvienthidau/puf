package xu.aa.fs;

import java.util.ArrayList;
import java.util.List;

import xu.aa.fs.ClubFileSystem.FixedLengthBlock;

/**
 * Player Management.
 * 
 * @author phong.nguyen
 *
 */
public class PlayerFileSystem {
	private static final char DELETE_INDICATOR = '?';
	private static final int NOT_EXIST_DELETED_BLOCK = -1;

	private int rootDeletePosition = NOT_EXIST_DELETED_BLOCK;
	private List<VariableLengthBlock> players = new ArrayList<>();

	class VariableLengthBlock {
		boolean deleted = false;
		int nextDeletePosition = NOT_EXIST_DELETED_BLOCK;
		int length = 0;
		int nextClubPosition = NOT_EXIST_DELETED_BLOCK;
		String content;
	}

	private ClubFileSystem clubFileSystem;

	public void add(String club, String name) {
		FixedLengthBlock clubBlock = clubFileSystem.exists(club);
		if (clubBlock == null) {
			return;
		}

	}

	public VariableLengthBlock exists(String name, int clubRootPosition) {
		int nextPosition = clubRootPosition;
		while (nextPosition != -1) {
			VariableLengthBlock block = players.get(nextPosition);
			if (!block.deleted) {

			}
		}
		return null;
	}

	public void setClubFileSystem(ClubFileSystem clubFileSystem) {
		this.clubFileSystem = clubFileSystem;
	}

}
