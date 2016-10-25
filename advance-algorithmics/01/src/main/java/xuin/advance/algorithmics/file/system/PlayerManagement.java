package xuin.advance.algorithmics.file.system;

public class PlayerManagement extends AbstractManagement {

    // assume that the total space needed to store players is less than 10,000 char
    // using following meta structure to store player
    // delete pattern: ?XXXX 4 chars follow ? store which next location deleted record
    // location 0000: mean there is no more delete record in system
    // assume that the maximum player name takes 2 chars to store
    // XXYYY is the pattern of alive record, in that XX is 2 number depict that the length of
    // player name, YYY is the club they play for.

    @Override
    public void add(String element) {

    }

    public void add(String club, String name) {
        int root = root();
        if (root == 0) {
            String meta = String.format("%02d", name.length()) + club;
            data.append(meta);
            data.append(name);
        }
    }

    @Override
    public void delete(String element) {
        // TODO Auto-generated method stub

    }

    @Override
    public void defragment() {
        // TODO Auto-generated method stub

    }

    @Override
    protected String emptyPattern() {
        return "0000";
    }

}
