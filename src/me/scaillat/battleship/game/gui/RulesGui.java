package me.scaillat.battleship.game.gui;

import me.scaillat.battleship.game.Game;

import javax.swing.*;
import java.awt.*;

public class RulesGui extends JFrame {

    private final Game game;

    public RulesGui(Game game) throws HeadlessException {
        this.game = game;

        this.setTitle("Battleship - Rules Menu");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(600, 300);
        this.setLayout(new BorderLayout());

        // Title
        JPanel titlePanel = new JPanel();
        JLabel title = new JLabel("Rules");
        title.setFont(new Font("Dialog", Font.BOLD, 16));
        titlePanel.add(title);
        this.getContentPane().add(BorderLayout.NORTH, titlePanel);

        // RULES
        JTextArea textArea = new JTextArea(
                "Game Concept: \n" +
                "\nBattleship is a war-themed board game for two players in which the opponents try to guess the location of the ships on one shared board. \n" +
                "\nGameplay: \n" +
                "\n2 Players play on one board that is set up by means of a grid filled with squares, each square is a location on which a ship can be placed. The 2 Players take turns clicking on a tile on the grid in an attempt to identify a square that contains a ship. If a location contains a ship, that part of the ship is hit, if not then It’s a miss. Once all parts of a ship has been hit, the ship will sink. The game continues until all ships have sunken. \n" +
                "\nSetting up the game\n" +
                "\n Before you are able to start the game, the board needs to be set up. This can be done by uploading a text file containing the board size and ship placement. The text file’s first line should be a positive integer for the board size. After that the file should list one ship per line with each line starting with the name of the ship followed by coordiantes. A coordinate needs to be specified by two numbers separated by a “*”.  Where the first number represents the row and second number representing the column.\n " +
                "\n There will always be 4  ships on the board, even if the gridsize is larger than the standard 8 by 8. The 4 ships contain one of each ship type. Being a carrier, battleship, cruiser, submarine or destroyer. Ships can’t be placed outside the border, nor can they overlap with other ships.\n" +
                "\nStarting the game.\n" +
                "\nAfter you have uploaded the text file, which doesn’t contain any errors, the game can start. When starting the game, the program will generate two random names with random idtags, identifying the players. The program will also randomly select one of the two players to start the game, after which both players take turns shooting on the grid. \n" +
                "\nScoring System: \n" +
                "\nEach player receives a set number of points for hitting a specific ship, and the usual amount of points received for a specific player hitting a specific ship is doubled if the hit sinks the ship. Players can choose from two scoring systems. In the regular scoring system, the point per hit are the same for each player. In the alternative system, the second player's points per hit are different in order to compensate for going second. \n"
        );
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setSize(500,300);
        JScrollPane scrollPane = new JScrollPane(textArea);
        this.getContentPane().add(BorderLayout.CENTER, scrollPane);

        // CLOSE BUTTON
        JPanel closePanel = new JPanel();
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        closePanel.add(closeButton);
        this.getContentPane().add(BorderLayout.SOUTH, closePanel);

        this.setVisible(true);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.game.getMainGameGui().setRulesGui(null);
    }
}
