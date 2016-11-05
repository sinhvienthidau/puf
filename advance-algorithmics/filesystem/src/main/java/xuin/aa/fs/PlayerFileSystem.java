package xuin.aa.fs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.commons.lang3.StringUtils;

import xuin.aa.block.ClubBlock;
import xuin.aa.block.PlayerBlock;

/**
 * manage player.bin file.
 * 
 * @author phong.nguyen
 */
public class PlayerFileSystem extends FileSystem {
    private ClubFileSystem clubFileSystem;

    private int fitMode = 0; // 0: first fit, 1: best fit.

    public PlayerFileSystem(String path) throws FileNotFoundException, IOException {
        super(path);
    }

    /**
     * add a player in to a club.
     * 
     * @param club
     *            club name: if club name don't exist in club.bin, nothing happen.
     * @param name
     *            player name: if player name already exist in player.bin with same club, nothing
     *            happen.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void add(String club, String name) throws FileNotFoundException, IOException {
        ClubBlock clubBlock = clubFileSystem.find(club);
        if (clubBlock != null) {
            PlayerBlock playerBlock = find(clubBlock.getRootPlayerIndex(), name);
            if (playerBlock == null) {
                int deletedRecordRoot = getDeletedRecordRoot();

                // if exist deleted record, detect the fit of record to store new player name.
                PlayerBlock deletedBlock = null;
                if (NOT_EXIST_DELETED_BLOCK != deletedRecordRoot) {
                    if (fitMode == 0) {
                        deletedBlock = getFirstFitBlock(name);
                    } else if (fitMode == 1) {
                        deletedBlock = getBestFitBlock(name);
                    }
                }

                if (deletedBlock == null) {
                    // in case don't have any place to overwrite then append new block at the end of
                    // file.
                    PlayerBlock block = new PlayerBlock();
                    block.setData(name);
                    block.setLength(name.length());
                    block.setNextClubPlayerIndex(clubBlock.getRootPlayerIndex());

                    clubBlock.setRootPlayerIndex(getLastOffset());
                    clubFileSystem.write(clubBlock.getOffset(), clubBlock.toBytes());

                    append(block.toBytes());
                } else {
                    // exist deleted record to overwrite then overwrite it.
                    deletedBlock.setDeleted(false);
                    deletedBlock.setData(name);
                    deletedBlock.setNextClubPlayerIndex(clubBlock.getRootPlayerIndex());

                    if (deletedBlock.getPreviousIndex() == NOT_EXIST_NEXT_BLOCK) {
                        updateDeleteRecordRoot(deletedBlock.getNextDeleteIndex());
                    } else {
                        PlayerBlock previousBlock = PlayerBlock.fromBytes(getBlock(deletedBlock.getPreviousIndex()),
                                deletedBlock.getPreviousIndex());
                        previousBlock.setNextDeleteIndex(deletedBlock.getNextDeleteIndex());

                        write(previousBlock.getOffset(), previousBlock.toBytes());
                    }

                    clubBlock.setRootPlayerIndex(deletedBlock.getOffset());
                    clubFileSystem.write(clubBlock.getOffset(), clubBlock.toBytes());

                    write(deletedBlock.getOffset(), deletedBlock.toBytes());
                }
            }
        }
    }

    private PlayerBlock getFirstFitBlock(String name) throws FileNotFoundException, IOException {
        int root = getDeletedRecordRoot();
        int previousIndex = -1;
        while (NOT_EXIST_DELETED_BLOCK != root) {
            PlayerBlock block = PlayerBlock.fromBytes(getBlock(root), root);
            block.setPreviousIndex(previousIndex);
            if (block.getLength() >= name.length()) {
                return block;
            }

            previousIndex = root;
            root = block.getNextDeleteIndex();
        }
        return null;
    }

    private PlayerBlock getBestFitBlock(String name) throws FileNotFoundException, IOException {
        int root = getDeletedRecordRoot();
        int previousIndex = -1;
        PlayerBlock bestBlock = null;
        while (NOT_EXIST_DELETED_BLOCK != root) {
            PlayerBlock block = PlayerBlock.fromBytes(getBlock(root), root);
            block.setPreviousIndex(previousIndex);
            if (block.getLength() == name.length()) {
                return block;
            } else if (block.getLength() > name.length()) {
                if (bestBlock == null) {
                    bestBlock = block;
                } else if (bestBlock.getLength() > block.getLength()) {
                    bestBlock = block;
                }
            }

            previousIndex = root;
            root = block.getNextDeleteIndex();
        }
        return bestBlock;
    }

    public void delete(String club, String name) throws FileNotFoundException, IOException {
        ClubBlock clubBlock = clubFileSystem.find(club);
        if (clubBlock != null) {
            PlayerBlock playerBlock = find(clubBlock.getRootPlayerIndex(), name);
            if (playerBlock != null) {
                playerBlock.setDeleted(true);
                playerBlock.setNextDeleteIndex(getDeletedRecordRoot());

                if (playerBlock.getPreviousIndex() == -1) {
                    clubFileSystem.updatePlayerRoot(club, playerBlock.getNextClubPlayerIndex());
                } else {
                    PlayerBlock previousBlock = PlayerBlock.fromBytes(getBlock(playerBlock.getPreviousIndex()),
                            playerBlock.getPreviousIndex());
                    previousBlock.setNextClubPlayerIndex(playerBlock.getNextClubPlayerIndex());

                    write(previousBlock.getOffset(), previousBlock.toBytes());
                }

                updateDeleteRecordRoot(playerBlock.getOffset());
                write(playerBlock.getOffset(), playerBlock.toBytes());
            }
        }
    }

    public void defrag() throws FileNotFoundException, IOException {
        // keep all available players the offset of club file system.
        // discard the link from these players to defragment file system
        // and using the club file system offset to recover that link later
        for (int o = Integer.BYTES; o < clubFileSystem.file.length(); o += ClubBlock.BYTES) {
            ClubBlock clubBlock = ClubBlock.fromBytes(clubFileSystem.read(o, ClubBlock.BYTES), o);
            if (!clubBlock.isDeleted()) {
                int root = clubBlock.getRootPlayerIndex();
                while (NOT_EXIST_DELETED_BLOCK != root) {
                    PlayerBlock playerBlock = PlayerBlock.fromBytes(getBlock(root), root);
                    root = playerBlock.getNextClubPlayerIndex();

                    playerBlock.setNextClubPlayerIndex(clubBlock.getOffset());

                    write(playerBlock.getOffset(), playerBlock.toBytes());

                    clubBlock.setRootPlayerIndex(-1);
                    clubFileSystem.write(clubBlock.getOffset(), clubBlock.toBytes());
                }
            }
        }

        // start to shift bytes
        int len = Long.valueOf(file.length()).intValue();
        for (int offset = 4; offset < len;) {
            PlayerBlock block = PlayerBlock.fromBytes(getBlock(offset), offset);
            int blockBytes = block.countBytes();

            int shift = 0;
            if (block.isDeleted()) {
                shift = block.countBytes();
            } else if (block.getData().contains("*")) {
                int fragments = StringUtils.countMatches(block.getData(), FRAGMENT_INDICATOR);
                block.setLength(block.getLength() - fragments);
                block.setData(StringUtils.remove(block.getData(), FRAGMENT_INDICATOR));
                write(block.getOffset(), block.toBytes());
                shift = fragments * 2;
            }

            if (shift != 0) {
                for (int o = offset + blockBytes; o < len;) {
                    PlayerBlock shiftBlock = PlayerBlock.fromBytes(getBlock(o), o);
                    shift(o, o - shift, shiftBlock.countBytes());

                    o = o + shiftBlock.countBytes();
                }

                trunc(shift);
                len = len - shift;
            }

            offset = offset + (blockBytes - shift);
        }

        // update root delete to null
        updateDeleteRecordRoot(NOT_EXIST_DELETED_BLOCK);

        // reconstruct the link between players and the club
        for (int offset = 4; offset < file.length();) {
            PlayerBlock playerBlock = PlayerBlock.fromBytes(getBlock(offset), offset);
            byte[] bytes = clubFileSystem.read(playerBlock.getNextClubPlayerIndex(), ClubBlock.BYTES);
            ClubBlock clubBlock = ClubBlock.fromBytes(bytes, playerBlock.getNextClubPlayerIndex());

            playerBlock.setNextClubPlayerIndex(clubBlock.getRootPlayerIndex());
            write(playerBlock.getOffset(), playerBlock.toBytes());

            clubBlock.setRootPlayerIndex(offset);
            clubFileSystem.write(clubBlock.getOffset(), clubBlock.toBytes());

            offset = offset + playerBlock.countBytes();
        }
    }

    public void transfer(String name, String fromClub, String toClub) {

    }

    public PlayerBlock find(int rootPlayerIndex, String name) throws FileNotFoundException, IOException {
        int root = rootPlayerIndex;
        int previousIndex = -1;
        while (NOT_EXIST_DELETED_BLOCK != root) {
            PlayerBlock block = PlayerBlock.fromBytes(getBlock(root), root);
            block.setPreviousIndex(previousIndex);
            if (StringUtils.remove(block.getData(), FRAGMENT_INDICATOR).equals(name)) {
                return block;
            }
            previousIndex = root;
            root = block.getNextClubPlayerIndex();
        }
        return null;
    }

    public boolean exists(String club, String name) throws FileNotFoundException, IOException {
        ClubBlock clubBlock = clubFileSystem.find(club);
        if (clubBlock != null) {
            return find(clubBlock.getRootPlayerIndex(), name) != null;
        }
        return false;
    }

    public byte[] getBlock(int offset) throws FileNotFoundException, IOException {
        byte[] bytes = read(offset + 1, Integer.BYTES);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int length = buffer.getInt();

        return read(offset, 13 + length * Character.BYTES);
    }

    public ClubFileSystem getClubFileSystem() {
        return clubFileSystem;
    }

    public void setClubFileSystem(ClubFileSystem clubFileSystem) {
        this.clubFileSystem = clubFileSystem;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        try {
            builder.append(getDeletedRecordRoot()).append('|');
            for (int i = 4; i < file.length();) {
                PlayerBlock block = PlayerBlock.fromBytes(getBlock(i), i);
                builder.append(block.toString());
                i = i + block.countBytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public int getFitMode() {
        return fitMode;
    }

    public void setFitMode(int fitMode) {
        this.fitMode = fitMode;
    }
}
