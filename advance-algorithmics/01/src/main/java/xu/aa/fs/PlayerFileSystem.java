package xu.aa.fs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xu.aa.fs.ClubFileSystem.FixedLengthBlock;

/**
 * Player Management.
 * 
 * @author phong.nguyen
 *
 */
public class PlayerFileSystem {
    private static final char DELETE_INDICATOR = '?';
    private static final char SEPARATOR_LENGHT_NEXT_ELEMENT = '/';
    private static final char FRAGMENT = '*';
    private static final int NOT_EXIST_DELETED_BLOCK = -1;

    private int byteLength = 1;
    private int rootDeletePosition = NOT_EXIST_DELETED_BLOCK;
    private List<VariableLengthBlock> players = new ArrayList<>();

    // transient field to convert from byte position to index
    Map<Integer, Integer> indexes = new HashMap<>();

    class VariableLengthBlock {
        boolean deleted = false;
        int nextDeletePosition = NOT_EXIST_DELETED_BLOCK;
        int length = 0;
        String club;
        int nextClubPosition = NOT_EXIST_DELETED_BLOCK;
        String content;

        // transient field to keep index
        int bytes = 0;
        int previousBlock = -1;
    }

    private ClubFileSystem clubFileSystem;

    public void add(String club, String name) {
        FixedLengthBlock clubBlock = clubFileSystem.exists(club);
        if (clubBlock == null || exists(name, clubBlock.rootClubPosition) != null) {
            return;
        }

        if (rootDeletePosition == NOT_EXIST_DELETED_BLOCK) {
            VariableLengthBlock block = new VariableLengthBlock();
            block.content = name;
            block.length = name.length();
            block.nextClubPosition = clubBlock.rootClubPosition;
            block.club = club;
            clubBlock.rootClubPosition = byteLength;
            indexes.put(byteLength, players.size());
            byteLength += name.length() + 5;

            players.add(block);
        } else {
            VariableLengthBlock block = players.get(indexes.get(rootDeletePosition));

        }
    }

    public void delete(String club, String name) {
        FixedLengthBlock clubBlock = clubFileSystem.exists(club);
        VariableLengthBlock playerBlock = exists(name, clubBlock.rootClubPosition);
        if (clubBlock == null || playerBlock == null) {
            return;
        }

        playerBlock.deleted = true;
        rootDeletePosition = playerBlock.bytes;
        clubBlock.rootClubPosition = playerBlock.previousBlock;
    }

    public VariableLengthBlock exists(String name, int clubRootPosition) {
        int conductPosition = clubRootPosition;
        int previousBlock = -1;
        while (conductPosition != -1) {
            VariableLengthBlock block = players.get(indexes.get(conductPosition));
            if (!block.deleted && blockCompare(block.content, name)) {
                block.bytes = conductPosition;
                block.previousBlock = previousBlock;
                return block;
            }
            previousBlock = conductPosition;

            conductPosition = block.nextClubPosition;
        }
        return null;
    }

    public void setClubFileSystem(ClubFileSystem clubFileSystem) {
        this.clubFileSystem = clubFileSystem;
    }

    private boolean blockCompare(String block, String name) {
        return block.equals(name);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        if (!players.isEmpty()) {
            int bytes = 1;
            builder.append(rootDeletePosition).append("|");
            for (VariableLengthBlock block : players) {
                builder.append("[").append(bytes).append("]");
                bytes += 5 + block.length;
                if (block.deleted) {
                    builder.append(DELETE_INDICATOR);
                    builder.append(block.length);
                    builder.append(SEPARATOR_LENGHT_NEXT_ELEMENT);
                    builder.append(block.nextDeletePosition);
                    builder.append(block.club);
                    builder.append(SEPARATOR_LENGHT_NEXT_ELEMENT);
                    builder.append(block.content);
                } else {
                    builder.append(block.length);
                    builder.append(SEPARATOR_LENGHT_NEXT_ELEMENT);
                    builder.append(block.nextClubPosition);
                    builder.append(block.club);
                    builder.append(SEPARATOR_LENGHT_NEXT_ELEMENT);
                    builder.append(block.content);
                }
                builder.append("|");
            }
        }

        return builder.toString();
    }
}
