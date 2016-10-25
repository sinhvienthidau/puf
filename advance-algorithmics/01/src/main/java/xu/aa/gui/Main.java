package xu.aa.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import xu.aa.fs.ClubManagement;
import xu.aa.fs.PlayerManagement;

public class Main extends JFrame {
    private static final long serialVersionUID = 1612571196676580081L;

    public static final int CLUB_VIEW = 0;
    public static final int PLAYER_VIEW = 1;
    public static final int ALL_VIEW = 2;

    // file system
    private ClubManagement club = new ClubManagement();
    private PlayerManagement player = new PlayerManagement();

    // Menu
    private JMenuBar menubar = new JMenuBar();
    private JMenu command = new JMenu("Command");
    private JMenuItem newClub = new JMenuItem("New Club");
    private JMenuItem close = new JMenuItem("Close");
    // console
    private JTextArea clubConsole = new JTextArea("Club File System");
    private JTextArea playerConsole = new JTextArea("Player File System");

    public Main() {
        // initialize file system
        club.setPlayer(player);
        club.setGui(this);
        player.setClub(club);
        player.setGui(this);

        newClub.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Component source = (Component) e.getSource();
                Object response = JOptionPane.showInputDialog(source, "Club's name:",
                        "Input information", JOptionPane.INFORMATION_MESSAGE, null, null, "");

                club.add((String) response);
            }
        });
        command.add(newClub);

        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        command.add(close);
        menubar.add(command);
        setJMenuBar(menubar);

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
    }

    public void printf(int view) {
        if (view == CLUB_VIEW || ALL_VIEW == view) {
            clubConsole.setText(clubConsole.getText() + "\n    " + club.toString());
        }
        if (view == PLAYER_VIEW || ALL_VIEW == view) {
            playerConsole.setText(playerConsole.getText() + "\n    " + player.toString());
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
