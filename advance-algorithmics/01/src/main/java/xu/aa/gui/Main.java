package xu.aa.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import xu.aa.fs.ClubFileSystem;
import xu.aa.fs.PlayerFileSystem;

public class Main extends JFrame {
    private static final long serialVersionUID = 1612571196676580081L;

    public static final int CLUB_VIEW = 0;
    public static final int PLAYER_VIEW = 1;
    public static final int ALL_VIEW = 2;

	private List<String> clubNames = new ArrayList<>();
	private Map<String, List<String>> playerMap = new HashMap<>();

    // file system
    private ClubFileSystem club = new ClubFileSystem();
    private PlayerFileSystem player = new PlayerFileSystem();

    // Menu
    private JMenuBar menubar = new JMenuBar();
    private JMenu commandMenu = new JMenu("Command");
	private JMenuItem commandClose = new JMenuItem("Close");

	private JMenu clubMenu = new JMenu("Club");
	private JMenuItem clubNew = new JMenuItem("New");
	private JMenuItem clubDelete = new JMenuItem("Delete");

    // console
    private JTextArea clubConsole = new JTextArea("Club File System");
    private JTextArea playerConsole = new JTextArea("Player File System");

    public Main() {
		/*
		 * Menus.
		 */

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
				if (clubName != null && clubName.length() == 3) {
					clubName = clubName.toUpperCase();
					if (clubNames.contains(clubName)) {
						console(CLUB_VIEW, "Club name already exists");
					} else {
						clubNames.add(clubName);
						club.add(clubName);
					}
				} else {
					console(CLUB_VIEW, "Club name must has 3 chars");
				}
			}
		});
		clubMenu.add(clubNew);

		clubDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Component source = (Component) e.getSource();
				Object response = JOptionPane.showInputDialog(source, "Club's name:", "Input information",
						JOptionPane.INFORMATION_MESSAGE, null, null, "");

				club.delete((String) response);
			}
		});
		clubMenu.add(clubDelete);

		menubar.add(clubMenu);

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
			clubConsole.setText(clubConsole.getText() + "\n\n" + club.toString());
        }
        if (view == PLAYER_VIEW || ALL_VIEW == view) {
			playerConsole.setText(playerConsole.getText() + "\n\n" + player.toString());
        }
    }

	public void console(int view, String msg) {
		if (view == CLUB_VIEW) {
			clubConsole.setText(clubConsole.getText() + "\n\n" + msg + ": ");
			clubConsole.setText(clubConsole.getText() + "\n" + club.toString());
		}
	}

    public static void main(String[] args) {
        new Main();
    }
}
