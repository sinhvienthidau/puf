package xuin.aa.gui;

import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import xuin.aa.fs.ClubFileSystem;

public class CustomDialog extends JPanel {
    private static final long serialVersionUID = -6927868554079758753L;

    private JTextField playerName;
    private JComboBox<String> clubs;
    private JComboBox<String> players;
    private ClubFileSystem clubFileSystem;

    private JComboBox<String> transferClubs;

    public CustomDialog(int mode) {
        playerName = new JTextField();
        clubs = new JComboBox<>();
        players = new JComboBox<>();
        transferClubs = new JComboBox<>();

        clubs.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                try {
                    players.removeAllItems();
                    for (String name : clubFileSystem.getPlayers((String) e.getItem())) {
                        players.addItem(name);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        setLayout(new GridLayout(3, 1));

        add(new JLabel("Input: "));
        if (mode == 0) {
            add(clubs);
            add(playerName);
        } else if (mode == 1) {
            add(clubs);
            add(players);
        } else if (mode == 2) {
            add(players);
            add(clubs);
            add(transferClubs);
        }
    }

    public JTextField getPlayerName() {
        return playerName;
    }

    public void setPlayerName(JTextField playerName) {
        this.playerName = playerName;
    }

    public JComboBox<String> getClubs() {
        return clubs;
    }

    public void setClubs(JComboBox<String> clubs) {
        this.clubs = clubs;
    }

    public JComboBox<String> getPlayers() {
        return players;
    }

    public void setPlayers(JComboBox<String> players) {
        this.players = players;
    }

    public ClubFileSystem getClubFileSystem() {
        return clubFileSystem;
    }

    public void setClubFileSystem(ClubFileSystem clubFileSystem) {
        this.clubFileSystem = clubFileSystem;
    }

    public JComboBox<String> getTransferClubs() {
        return transferClubs;
    }

    public void setTransferClubs(JComboBox<String> transferClubs) {
        this.transferClubs = transferClubs;
    }

}
