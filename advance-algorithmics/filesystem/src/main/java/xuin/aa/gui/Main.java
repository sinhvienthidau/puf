package xuin.aa.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.apache.commons.lang3.StringUtils;

import xuin.aa.fs.ClubFileSystem;
import xuin.aa.fs.PlayerFileSystem;

public class Main extends JFrame {
    private static final long serialVersionUID = 1612571196676580081L;

    public static final int CLUB_VIEW = 0;
    public static final int PLAYER_VIEW = 1;
    public static final int ALL_VIEW = 2;

    // file system
    private ClubFileSystem clubFileSystem;
    private PlayerFileSystem playerFileSystem;

    // Menu
    private JMenuBar menubar = new JMenuBar();
    private JMenu commandMenu = new JMenu("Command");
    private JMenuItem commandReset = new JMenuItem("Reset");
    private JMenuItem commandClose = new JMenuItem("Close");

    private JMenu clubMenu = new JMenu("Club");
    private JMenuItem clubNew = new JMenuItem("New");
    private JMenuItem clubDelete = new JMenuItem("Delete");
    private JMenuItem clubDefrag = new JMenuItem("Defragment");

    private JMenu playerMenu = new JMenu("Player");
    private JMenuItem playerNew = new JMenuItem("New");
    private JMenuItem playerDelete = new JMenuItem("Delete");
    private JMenuItem playerDefrag = new JMenuItem("Defragment");

    // console
    private JTextArea clubConsole = new JTextArea("Club File System");
    private JTextArea playerConsole = new JTextArea("Player File System");

    public Main() throws FileNotFoundException, IOException {
        clubFileSystem = new ClubFileSystem("club.bin");
        playerFileSystem = new PlayerFileSystem("player.bin");
        clubFileSystem.setPlayerFileSystem(playerFileSystem);
        playerFileSystem.setClubFileSystem(clubFileSystem);

        printf(ALL_VIEW);

        /*
         * Menus.
         */

        commandReset.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                File club = new File("club.bin");
                club.delete();

                try (FileOutputStream os = new FileOutputStream(club)) {
                    ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
                    buffer.putInt(-1);
                    os.write(buffer.array());
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                File player = new File("player.bin");
                player.delete();

                try (FileOutputStream os = new FileOutputStream(player)) {
                    ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
                    buffer.putInt(-1);
                    os.write(buffer.array());
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                printf(ALL_VIEW);
            }
        });
        commandMenu.add(commandReset);

        commandClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        commandMenu.add(commandClose);

        menubar.add(commandMenu);

        clubNew.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Component source = (Component) e.getSource();
                Object response = JOptionPane.showInputDialog(source, "Club's name:", "Input information",
                        JOptionPane.INFORMATION_MESSAGE, null, null, "");

                String clubName = (String) response;
                if (clubName != null) {
                    if (clubName.length() == 3) {
                        clubName = clubName.toUpperCase();
                        try {
                            if (clubFileSystem.exist(clubName)) {
                                console(CLUB_VIEW, "Club: " + clubName + " already exist.");
                            } else {
                                clubFileSystem.add(clubName);
                                printf(CLUB_VIEW);
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        console(CLUB_VIEW, "Club name must has 3 chars");
                    }
                }
            }
        });
        clubMenu.add(clubNew);

        clubDelete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Component source = (Component) e.getSource();
                try {
                    Object response = JOptionPane.showInputDialog(source, "Club's name:", "Input information",
                            JOptionPane.PLAIN_MESSAGE, null, clubFileSystem.getClubs().toArray(), "");
                    if (response == null || StringUtils.isNotEmpty((String) response)) {
                        clubFileSystem.delete((String) response);
                        printf(ALL_VIEW);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        clubMenu.add(clubDelete);

        clubDefrag.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    clubFileSystem.defrag();
                    printf(CLUB_VIEW);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        clubMenu.add(clubDefrag);

        menubar.add(clubMenu);

        playerNew.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                CustomDialog dialog = new CustomDialog(0);
                dialog.setClubFileSystem(clubFileSystem);

                try {
                    for (String item : clubFileSystem.getClubs()) {
                        dialog.getClubs().addItem(item);
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                }

                Component source = (Component) e.getSource();
                int response = JOptionPane.showConfirmDialog(source, dialog, "Input Confirmation",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);

                if (response == JOptionPane.OK_OPTION) {
                    try {
                        String playerName = dialog.getPlayerName().getText();
                        if (StringUtils.isNotEmpty(playerName)) {
                            String clubName = (String) dialog.getClubs().getSelectedItem();
                            if (playerFileSystem.exists(clubName, playerName)) {
                                console(PLAYER_VIEW, "Player: " + playerName + "in Club: " + clubName + " already exist.");
                            } else {
                                playerFileSystem.add(clubName, playerName);
                                printf(ALL_VIEW);
                            }
                        } else {
                            console(CLUB_VIEW, "Player name: null");
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        playerMenu.add(playerNew);

        playerDelete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                CustomDialog dialog = new CustomDialog(1);
                dialog.setClubFileSystem(clubFileSystem);

                try {
                    for (String item : clubFileSystem.getClubs()) {
                        dialog.getClubs().addItem(item);
                    }

                    Component source = (Component) e.getSource();
                    int response = JOptionPane.showConfirmDialog(source, dialog, "Input Confirmation",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);

                    if (response == JOptionPane.OK_OPTION) {
                        String clubName = (String) dialog.getClubs().getSelectedItem();
                        String playerName = (String) dialog.getPlayers().getSelectedItem();
                        if (StringUtils.isNotEmpty(clubName) && StringUtils.isNotEmpty(playerName)) {
                            playerFileSystem.delete(clubName, playerName);
                            printf(ALL_VIEW);
                        }
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        });
        playerMenu.add(playerDelete);

        playerDefrag.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    playerFileSystem.defrag();
                    printf(ALL_VIEW);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        playerMenu.add(playerDefrag);

        menubar.add(playerMenu);

        setJMenuBar(menubar);

        /*
         * Consoles.
         */

        clubConsole.setBackground(Color.BLACK);
        clubConsole.setForeground(Color.RED);
        clubConsole.setEditable(false);
        clubConsole.setLineWrap(true);
        add(clubConsole);

        playerConsole.setBackground(Color.BLACK);
        playerConsole.setForeground(Color.RED);
        playerConsole.setEditable(false);
        playerConsole.setLineWrap(true);
        add(playerConsole);

        setSize(1200, 600);
        setLayout(new GridLayout(1, 2));
        setTitle("Assignment 01");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public void printf(int view) {
        if (view == CLUB_VIEW || ALL_VIEW == view) {
            clubConsole.setText(clubConsole.getText() + "\n\n" + clubFileSystem.toString());
        }
        if (view == PLAYER_VIEW || ALL_VIEW == view) {
            playerConsole.setText(playerConsole.getText() + "\n\n" + playerFileSystem.toString());
        }
    }

    public void console(int view, String msg) {
        if (view == CLUB_VIEW) {
            clubConsole.setText(clubConsole.getText() + "\n\n" + msg + ": ");
            clubConsole.setText(clubConsole.getText() + "\n" + clubFileSystem.toString());
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        new Main();
    }
}
