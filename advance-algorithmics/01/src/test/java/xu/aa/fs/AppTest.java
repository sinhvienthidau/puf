package xu.aa.fs;

import org.junit.Test;

public class AppTest {
    @Test
    public void testClub() {
        ClubFileSystem club = new ClubFileSystem();

        club.add("MAN");
        club.add("CHE");
        club.add("HUK");
        club.add("LEI");

        club.delete("CHE");
        club.delete("MAN");

        club.add("STO");

        System.out.println(club.toString());

		club.defrag();

		System.out.println(club.toString());
    }

    @Test
    public void testPlayer() {
    }
}
