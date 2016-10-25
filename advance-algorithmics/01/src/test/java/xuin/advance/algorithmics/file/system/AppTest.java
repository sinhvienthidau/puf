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
		ClubManagement club = new ClubManagement();
		PlayerManagement player = new PlayerManagement();
		player.setClub(club);
		club.setPlayer(player);

		club.add("MAN");
		club.add("CHE");
		club.add("HUK");
		club.add("LEI");

		club.delete("CHE");

		System.out.println(club.toString());

		player.add("MAN", "Rooney");
		player.add("MAN", "Ramos");
		player.add("MAN", "Garath Bale");

		System.out.println(club.toString());
        System.out.println(player.toString());

		player.delete("MAN", "Ramos");

		System.out.println(club.toString());
		System.out.println(player.toString());

		player.delete("MAN", "Garath Bale");

		System.out.println(club.toString());
		System.out.println(player.toString());

		player.add("MAN", "Owen");
		player.add("MAN", "Ibrahamovic");
		player.delete("MAN", "Owen");

		System.out.println(club.toString());
		System.out.println(player.toString());

		club.delete("MAN");

		System.out.println(club.toString());
		System.out.println(player.toString());
    }
}
