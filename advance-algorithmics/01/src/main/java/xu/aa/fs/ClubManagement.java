package xu.aa.fs;

import xu.aa.gui.Main;

/**
 * Management Club.
 * 
 * @author phong.nguyen
 *
 */
public class ClubManagement extends AbstractManagement {
    private static final String FORMAT_3DIGITS = "%03d";
    private static final int BLOCK = 8;

    private PlayerManagement player;

    // {meta.data}
    // [root of deleted element]{[content block]}
    //
    // [root of deleted element]: pattern X{3}, 3 digits depict location of root deleted element.
    // 000 indicates that there is no more deleted record in file.
    // [content block]: using 8 digits
    // pattern 1: +XXXYYYY depict a visible record. X{3} club annotation, Y{4} root element of this
    // club in player file.
    // pattern 2: -XXXYYYY depict a deleted record. X{3} is 3 digits of next deleted record, Y{4}
    // remain spare spaces, this information is waste spaces, but we need to keep this information
    // to ensure the length of block is fixed length.

    public void add(String club) {
        if (find(club)[0] == 0) {
            int root = root();
            String content = VISIBLE_INDICATOR + club + player.emptyPattern();
            if (root == 0) {
                // in case don't have any deleted record yet, append new record into the end.
                data.append(content);
            } else {
                // overwrite record
                data.replace(0, 3, data.substring(root + 1, root + 4));
                data.replace(root, root + BLOCK, content);
            }

            gui.printf(Main.CLUB_VIEW);
        }
    }

    public void delete(String club) {
        int found = find(club)[0];
        if (found != 0) {
            data.setCharAt(found, DELETED_INDICATOR);

            int position = Integer.parseInt(data.substring(found + 4, found + 8));
            data.replace(found + 1, found + 4, String.format(FORMAT_3DIGITS, root()));
            data.replace(found + 4, found + 8, player.emptyPattern());
            data.replace(0, 3, String.format(FORMAT_3DIGITS, found));

            // delete all player belong to a club, by passing root node to delete entire linked
            // list.
            player.deletes(position);
        }
    }

    public void defragment() {
        // reset root to null.
        data.replace(0, 3, emptyPattern());
        for (int i = startLocation(); i < data.length(); i = i + BLOCK) {
            if (DELETED_INDICATOR == data.charAt(i)) {
                // this step in this sample will run very fast, because it on memory
                // in fact, for the file system this step take very long time to shift
                // memory to the new position.
                data.replace(i, i + BLOCK, "");
                // after remove record, reset the counter.
                i = i - BLOCK;
            }
        }
    }

    public int[] find(String club) {
        // 0: position of club in club file.
        // 1: position of root club in player file.
        int[] positions = new int[2];
        positions[0] = 0;
        positions[0] = 0;

        for (int i = startLocation(); i < data.length(); i = i + BLOCK) {
            if (DELETED_INDICATOR != data.charAt(i)) {
                if (data.subSequence(i + 1, i + 4).equals(club)) {
                    positions[0] = i;
                    positions[1] = Integer.parseInt(data.substring(i + 4, i + 8));
                    return positions;
                }
            }
        }

        return positions;
    }

    public void updateClubRoot(int index, int position) {
        data.replace(index + 4, index + 8, String.format("%04d", position));
    }

    @Override
    protected String emptyPattern() {
        return "000";
    }

    public void setPlayer(PlayerManagement player) {
        this.player = player;
    }

}
