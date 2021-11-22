package me.scaillat.battleship.game.gui;

import me.scaillat.battleship.game.Game;
import me.scaillat.battleship.objects.Location;
import me.scaillat.battleship.objects.Status;
import me.scaillat.battleship.game.ScoreType;
import me.scaillat.battleship.objects.Player;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

public class GameGui extends JFrame {

    private final Game game;
    private final int boardSize;
    private final Location[][] board;
    private final Set<JButton> buttonSet;

    private JLabel turnLabel;
    private HighScoreGui highScoreGui;


    public GameGui(Game game) throws HeadlessException {
        this.game = game;
        this.board = game.getGameBoard().getBoard();
        this.boardSize = board.length;
        this.buttonSet = new HashSet<>();

        this.setTitle("Battleship - Game Menu");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(600, 600);
        this.setPreferredSize(new Dimension(600, 600));
        this.setLayout(new BorderLayout());

        this.getContentPane().add(BorderLayout.NORTH, getHeader());
        this.getContentPane().add(BorderLayout.CENTER, getBoard());
        this.setVisible(true);
    }

    private JPanel getHeader() {
        JPanel headerPanel = new JPanel(new GridLayout(2, 5));

        // BUTTONS
        JButton highScoreButton = new JButton("High Scores");
        highScoreButton.addActionListener(e -> {
            if (highScoreGui == null) {
                highScoreGui = new HighScoreGui(game);
            }
        });

        JButton quitButton = new JButton("Quit Game");
        quitButton.addActionListener(e -> dispose());

        // LABELS
        JLabel player1ScoreTitle = new JLabel(game.getPlayer1().getUserName());
        JLabel turnTitle = new JLabel("Turn:");
        JLabel player2ScoreTitle = new JLabel(game.getPlayer2().getUserName());

        // DYNAMIC LABELS
        this.game.getPlayer1().getScoreLabel().setText("0");
        this.turnLabel = new JLabel(this.game.getCurrentPlayer().getUserName());
        this.game.getPlayer2().getScoreLabel().setText("0");

        headerPanel.add(highScoreButton);
        headerPanel.add(player1ScoreTitle);
        headerPanel.add(turnTitle);
        headerPanel.add(player2ScoreTitle);
        headerPanel.add(quitButton);
        headerPanel.add(new JPanel()); // empty grid cell
        headerPanel.add(this.game.getPlayer1().getScoreLabel());
        headerPanel.add(this.turnLabel);
        headerPanel.add(this.game.getPlayer2().getScoreLabel());
        headerPanel.add(new JPanel()); // empty grid cell

        return headerPanel;
    }

    private JPanel getBoard() {
        GridLayout gridLayout = new GridLayout(this.boardSize, this.boardSize);
        JPanel boardPanel = new JPanel(gridLayout);

        for (int i = 0; i < this.boardSize * this.boardSize; i++) {
            JButton button = new JButton();
            button.setBackground(Color.GRAY);
            button.setBorder(new LineBorder(Color.WHITE, 2));

            Location location = this.game.getGameBoard().getCoordsFromPosition(i);
            button.setToolTipText("(" + (location.getX() + 1) + "," + (location.getY() + 1) + ")");

            button.addActionListener(e -> {
                Location loc = this.board[location.getX()][location.getY()];
                if (loc == null) return;

                tileClick(e, loc);
            });

            this.buttonSet.add(button);
            boardPanel.add(button);
        }

        return boardPanel;
    }

    private void tileClick(ActionEvent e, Location loc) {
        JButton button = (JButton) e.getSource();

        if (!loc.isUnknown()) {
            return;
        }

        Player currentPlayer = game.getCurrentPlayer();

        if (loc.isShip()) {
            switch (loc.getShip().getShipType()) {
                case SUBMARINE:
                    button.setBackground(Color.GREEN);
                    break;
                case BATTLESHIP:
                    button.setBackground(Color.MAGENTA);
                    break;
                case DESTROYER:
                    button.setBackground(Color.YELLOW);
                    break;
                case CARRIER:
                    button.setBackground(Color.RED);
                    break;
            }
            loc.setStatus(Status.HIT);

            int addScore = 0;
            if (this.game.getScoreManager().getScoreType() == ScoreType.ALTERNATIVE) {
                if (!loc.getShip().isDiscovered()) {
                    loc.getShip().setDiscoveredShipPlayer(currentPlayer);
                }

                if (loc.getShip().getDiscoveredShipPlayer().equals(currentPlayer)) {
                    addScore = loc.getShip().getShipType().getHitValue();
                } else {
                    addScore = loc.getShip().getShipType().getHitValue() / 2;
                }

                if (loc.getShip().isSunk()) {
                    addScore += addScore;
                }

            } else {
                if (loc.getShip().isSunk()) {
                    addScore = loc.getShip().getShipType().getHitValue();

                }
                addScore += loc.getShip().getShipType().getHitValue();
            }
            currentPlayer.addScore(addScore);

        } else {
            button.setBackground(new Color(85, 185, 218));
            loc.setStatus(Status.MISS);
        }

        // Handle Win
        if (isWin()) {
            for (JButton jButton : this.buttonSet) {
                jButton.removeActionListener(jButton.getActionListeners()[0]);
            }
            Player winner;
            if(this.game.getPlayer1().getScore() > this.game.getPlayer2().getScore()) {
                winner = this.game.getPlayer1();
            } else {
                winner = this.game.getPlayer2();
            }
            this.game.getScoreManager().addHighScore(winner.getUserName(), winner.getScore());

            JOptionPane.showMessageDialog(null, "Battleship Victory!" +
                    "\nWinner: " + winner.getUserName() + " -> " + winner.getScore(), "Victory!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Change Player
        this.game.getGameBoard().changePlayer();
        this.turnLabel.setText(this.game.getCurrentPlayer().getUserName());
    }

    private boolean isWin() {
        Location loc;
        for (int x = 0; x < this.boardSize; x++) {
            for (int y = 0; y < this.boardSize; y++) {
                loc = this.board[x][y];
                if (loc == null) continue;

                if (loc.isShip() && !loc.isHit()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setHighScoreGui(HighScoreGui highScoreGui) {
        this.highScoreGui = highScoreGui;
    }

    @Override
    public void dispose() {
        super.dispose();
        this.game.getMainGameGui().setGameGui(null);
        this.game.getPlayer1().setScore(0);
        this.game.getPlayer2().setScore(0);
    }
}
