package xuin.advance.algorithmics.file.system;

import org.junit.Test;

public class ClubManagementTest {
    @Test
    public void test() {
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
}
