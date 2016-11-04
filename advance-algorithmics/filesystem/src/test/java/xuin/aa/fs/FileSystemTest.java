package xuin.aa.fs;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;

public class FileSystemTest {
    @After
    public void tearDown() {
        File club = new File("club.bin");
        club.delete();

        File player = new File("player.bin");
        player.delete();
    }

    @Test
    public void test() throws IOException {
        ClubFileSystem club = new ClubFileSystem("club.bin");
        PlayerFileSystem player = new PlayerFileSystem("player.bin");
        player.setClubFileSystem(club);
        club.setPlayerFileSystem(player);

        club.add("MAN");
        club.add("CHE");
        club.add("LIV");
        club.add("ARS");

        System.out.println(club.toString());

        club.delete("CHE");

        System.out.println(club.toString());

        club.add("TOT");

        System.out.println(club.toString());

        club.delete("LIV");
        club.delete("MAN");

        System.out.println(club.toString());


        player.add("ARS", "Mesut Ozil");
        player.add("ARS", "Alexis Sanchez");
        player.add("ARS", "Oliver Giroud");
        player.add("ARS", "Theo Walcott");
        player.add("TOT", "Harry Kane");
        player.add("TOT", "Erik Lamela");

        System.out.println(player.toString());

        player.delete("ARS", "Oliver Giroud");
        player.delete("ARS", "Mesut Ozil");

        System.out.println(player.toString());

        player.add("ARS", "Lucas Perez");

        System.out.println(player.toString());

        club.defrag();
        player.defrag();

        System.out.println(club.toString());
        System.out.println(player.toString());

        club.delete("TOT");

        System.out.println(club.toString());
        System.out.println(player.toString());
    }
}
