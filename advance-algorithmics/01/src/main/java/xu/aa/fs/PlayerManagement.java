package xu.aa.fs;

/**
 * Player Management.
 * 
 * @author phong.nguyen
 *
 */
public class PlayerManagement extends AbstractManagement {
    private ClubManagement club;

    // 0: first fit
    // 1: best fit
    private int mode = 0;

    // {meta.data}
    // [root of deleted element]{[content block]}
    //
    // [root of deleted element]: pattern XXXX, 4 digits depict location of root deleted element.
    // 0000 indicates that there is no more deleted record in file.
    //
    // [content block]: pattern XYYCCCZZZZ{content}. 10 chars for content header.
    // X is indicator for deleted or visible '-' for deleted '+' mean visible.
    // YY is the length of {content}.
    // CCC stand for club annotation <- this information is not need to stored cause we get root
    // element already depicted it, but for perspective purpose in this implementation we keep it.
    // ZZZZ is the next location which stored the same club player, using with visible indicator.
    // ZZZZ is the next location which stored deleted record, using with deleted indicator.
    // {content}: player name

    public void add(String club, String name) {
        // position of root record for this club in player file.
        int[] founds = this.club.find(club);
        if (founds[0] == 0) {
            // not found club in file
            return;
        }

        if (root() != 0) {
            int fit = 0;
            int position = root();
            // name of player have less than 100 chars
            int minimum = 100;
            do {
                int contentSize = Integer.parseInt(data.substring(position + 1, position + 3));
                if (contentSize == name.length()) {
                    fit = position;
                    break;
                } else if (contentSize > name.length()) {
                    if (mode == 1) {
                        // in best fit, store the minimum fit.
                        if (minimum > contentSize - name.length()) {
                            fit = position;
                            minimum = contentSize - name.length();
                        }
                    } else if (mode == 0) {
                        // in first fit mode, return first found.
                        fit = position;
                        break;
                    }
                }

                position = Integer.parseInt(data.substring(position + 6, position + 10));
            } while (position != 0);

            if (fit != 0) {
                data.replace(0, 4, data.substring(fit + 6, fit + 10));
                // in case of found a place to overwrite data
                // overwrite data but keep the same structure of old data.
                // changing structure will cause unfortunately defragment process.
                int sizeOfFit = Integer.parseInt(data.substring(fit + 1, fit + 3));
                String meta = VISIBLE_INDICATOR + String.format("%02d", sizeOfFit) + club
                        + String.format("%04d", founds[1]);
                data.replace(fit, fit + 10, meta);
                data.replace(fit + 10, fit + 10 + sizeOfFit, expand(name, sizeOfFit));

                this.club.updateClubRoot(founds[0], fit);

                return;
            }
        }

        // when don't have deleted record to overwrite or don't find any record have enough space to
        // write, then write it to the end.
        // this length will be update as the root record for club in club file.
        int length = data.length();
        String meta = VISIBLE_INDICATOR + String.format("%02d", name.length()) + club
                + String.format("%04d", founds[1]);
        data.append(meta);
        data.append(name);

        // update root record for club in club file.
        this.club.updateClubRoot(founds[0], length);
    }

    public void delete(String club, String name) {
        int[] positions = find(club, name);
        int position = positions[0];
        if (position != 0) {
            data.setCharAt(position, DELETED_INDICATOR);

            // stored club position to reconstruct club linked list.
            // reconstruct club linked here will reduce defragment complexity.
            int clubPosition = Integer.parseInt(data.substring(position + 6, position + 10));
            if (positions[1] != 0) {
                // update node
                data.replace(positions[1] + 6, positions[1] + 10, String.format("%04d", clubPosition));
            } else {
                // update root
                this.club.updateClubRoot(this.club.find(club)[0], clubPosition);
            }

            data.replace(position + 6, position + 10, String.format("%04d", root()));
            data.replace(0, 4, String.format("%04d", position));
        }
    }

    public void deletes(int club) {
        int position = club;
        while (position != 0) {
            int next = Integer.parseInt(data.substring(position + 6, position + 10));
            data.setCharAt(position, DELETED_INDICATOR);
            data.replace(position + 6, position + 10, data.substring(0, 4));
            data.replace(0, 4, String.format("%04d", position));

            position = next;
        }
    }

    public void defragment() {
        // TODO Auto-generated method stub

    }

    private int[] find(String club, String name) {
        // 0: the actual node matching condition.
        // 1: the previous location of this node.
        int[] nodes = new int[2];
        // 1=0: point to root node.
        nodes[1] = 0;

        // position of root record for this club in player file.
        int[] founds = this.club.find(club);

        // loop till the end of linked list.
        int position = founds[1];
        if (position != 0) {
            String nextPostionMeta;
            do {
                int contentSize = Integer.parseInt(data.substring(position + 1, position + 3));
                // fragment comparison.
                if (data.substring(position + 10, position + 10 + contentSize).matches(name + "[.]*")) {
                    nodes[0] = position;
                    return nodes;
                }

                // if not match, move to next node, update previous node.
                nodes[1] = position;

                nextPostionMeta = data.substring(position + 6, position + 10);
                position = Integer.parseInt(nextPostionMeta);
            } while (!emptyPattern().equals(nextPostionMeta));
        }
        // zero means not found.
        nodes[0] = 0;
        nodes[1] = 0;
        return nodes;
    }

    @Override
    protected String emptyPattern() {
        return "0000";
    }

    public void setClub(ClubManagement club) {
        this.club = club;
    }

    private String expand(String name, int size) {
        StringBuilder builder = new StringBuilder(name);
        for (int i = 0; i < size - name.length(); i++) {
            builder.append('.');
        }
        return builder.toString();
    }
}
