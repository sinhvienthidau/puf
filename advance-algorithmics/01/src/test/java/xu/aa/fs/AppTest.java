package xu.aa.fs;

import org.junit.Test;

public class AppTest {
    @Test
    public void testClub() {
        AbstractFileSystem<FixedLengthBlock> club = new FixedLengthFileSystem();

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
		AbstractFileSystem<VaraiableLengthBlock> player = new VariableLengthFileSystem();
		player.add("Xavier Heznades");
		player.add("Wayner Rooney");
		player.add("Cistiano Ronaldo");
		player.add("Hoe Hart");

		player.delete("Xavier Heznades");
		player.delete("Cistiano Ronaldo");

		player.add("Gareth Bale");

		System.out.println(player.toString());

		player.defrag();

		System.out.println(player.toString());
    }
}
