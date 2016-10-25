package xuin.advance.algorithmics.file.system;

import org.junit.Test;

public class AppTest {
    @Test
    public void testClub() {
        ClubManagement management = new ClubManagement();

        management.add("MAN");
        management.add("CHE");
        management.add("HUK");
        management.add("LEI");

        management.delete("CHE");
        management.delete("MAN");

        management.add("STO");

        management.delete("STO");

        System.out.println(management.toString());

        management.defragment();

        System.out.println(management.toString());
    }

    @Test
    public void testPlayer() {
        PlayerManagement management = new PlayerManagement();

        management.add("RAE", "Carlos");
        management.add("RAE", "Ramos");
        management.add("RAE", "Garath Bale");

        System.out.println(management.toString());
    }
}
