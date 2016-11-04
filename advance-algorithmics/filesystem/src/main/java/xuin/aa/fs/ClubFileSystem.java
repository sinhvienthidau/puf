package xuin.aa.fs;

import java.io.FileNotFoundException;
import java.io.IOException;

import xuin.aa.block.AbstractBlock;
import xuin.aa.block.ClubBlock;
import xuin.aa.block.PlayerBlock;

public class ClubFileSystem extends FileSystem {
    private PlayerFileSystem playerFileSystem;

    public ClubFileSystem(String path) throws FileNotFoundException, IOException {
        super(path);
    }

    public void add(String name) throws FileNotFoundException, IOException {
        AbstractBlock matcher = find(name);
        if (matcher == null) {
            int deletedRecordRoot = getDeletedRecordRoot();
            if (NOT_EXIST_DELETED_BLOCK == deletedRecordRoot) {
                ClubBlock block = new ClubBlock();
                block.setData(name);

                append(block.toBytes());
            } else {
                ClubBlock block = ClubBlock.fromBytes(read(deletedRecordRoot, ClubBlock.BYTES), deletedRecordRoot);
                updateDeleteRecordRoot(block.getNextDeleteIndex());
                block.setDeleted(false);
                block.setNextDeleteIndex(NOT_EXIST_DELETED_BLOCK);
                block.setRootPlayerIndex(NOT_EXIST_DELETED_BLOCK);
                block.setData(name);

                write(block.getOffset(), block.toBytes());
            }
        }
    }

    public void delete(String name) throws FileNotFoundException, IOException {
        ClubBlock block = find(name);
        if (block != null) {
            int root = block.getRootPlayerIndex();
            while (NOT_EXIST_DELETED_BLOCK != root) {
                PlayerBlock playerBlock = PlayerBlock.fromBytes(playerFileSystem.getBlock(root), root);
                playerBlock.setDeleted(true);
                playerBlock.setNextDeleteIndex(playerFileSystem.getDeletedRecordRoot());

                playerFileSystem.updateDeleteRecordRoot(root);
                root = playerBlock.getNextClubPlayerIndex();

                playerBlock.setNextClubPlayerIndex(-1);

                playerFileSystem.write(playerBlock.getOffset(), playerBlock.toBytes());
            }

            block.setNextDeleteIndex(getDeletedRecordRoot());
            block.setRootPlayerIndex(-1);
            block.setDeleted(true);
            updateDeleteRecordRoot(block.getOffset());

            write(block.getOffset(), block.toBytes());
        }
    }

    public void updatePlayerRoot(String name, int offset) throws FileNotFoundException, IOException {
        ClubBlock block = find(name);
        if (block != null) {
            block.setRootPlayerIndex(offset);

            write(block.getOffset(), block.toBytes());
        }
    }

    public void defrag() throws FileNotFoundException, IOException {
        updateDeleteRecordRoot(NOT_EXIST_DELETED_BLOCK);
        for (int offset = getLastOffset() - ClubBlock.BYTES; offset >= Integer.BYTES; offset = offset - ClubBlock.BYTES) {
            AbstractBlock block = ClubBlock.fromBytes(read(offset, ClubBlock.BYTES), offset);
            if (block.isDeleted()) {
                for (int o = offset; o < file.length(); o = o + ClubBlock.BYTES) {
                    shift(o + ClubBlock.BYTES, o, ClubBlock.BYTES);
                }
                trunc(ClubBlock.BYTES);
            }
        }
    }

    public ClubBlock find(String name) throws FileNotFoundException, IOException {
        for (int offset = Integer.BYTES; offset < file.length(); offset += ClubBlock.BYTES) {
            ClubBlock block = ClubBlock.fromBytes(read(offset, ClubBlock.BYTES), offset);
            if (!block.isDeleted() && block.getData().equals(name)) {
                return block;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        try {
            builder.append(getDeletedRecordRoot()).append('|');
            for (int offset = Integer.BYTES; offset < file.length(); offset += ClubBlock.BYTES) {
                AbstractBlock block = ClubBlock.fromBytes(read(offset, ClubBlock.BYTES), offset);
                builder.append(block.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public void setPlayerFileSystem(PlayerFileSystem playerFileSystem) {
        this.playerFileSystem = playerFileSystem;
    }

}
