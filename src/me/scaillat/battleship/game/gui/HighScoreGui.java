package me.scaillat.battleship.game.gui;

import me.scaillat.battleship.game.Game;
import me.scaillat.battleship.objects.HighScore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class HighScoreGui extends JFrame {

    private final Game game;

    public HighScoreGui(Game game) throws HeadlessException {
        this.game = game;

        this.setTitle("Battleship - High Scores Menu");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(600, 300);
        this.setLayout(new BorderLayout());

        // Title
        JPanel titlePanel = new JPanel();
        JLabel title = new JLabel("High Scores");
        title.setFont(new Font("Dialog", Font.BOLD, 16));
        titlePanel.add(title);
        this.getContentPane().add(BorderLayout.NORTH, titlePanel);


        // HIGH SCORES
        JTable table = new JTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Load Values
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("Username");
        model.addColumn("High Score");

        for (HighScore highScore : this.game.getScoreManager().getHighScores()) {
            String[] row = new String[2];
            row[0] = highScore.getUsername();
            row[1] = String.valueOf(highScore.getValue());
            model.addRow(row);
        }

        table.setModel(model);
        JScrollPane highScoresScrollPane = new JScrollPane(table);
        this.getContentPane().add(BorderLayout.CENTER, highScoresScrollPane);

        //FOOTER with CLOSE and RESET BUTTON
        JPanel footPanel = new JPanel(new FlowLayout());

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        JButton resetButton = new JButton("Reset Scores");
        resetButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.getScoreManager().resetHighScore();

                int rowCount = model.getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    model.removeRow(i);
                }
            }
        });

        footPanel.add(closeButton);
        footPanel.add(resetButton);

        this.getContentPane().add(BorderLayout.SOUTH, footPanel);
        this.setVisible(true);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.game.getMainGameGui().setHighScoreGui(null);

        GameGui gameGui = this.game.getGameBoard().getGameGui();
        if (gameGui != null) {
            gameGui.setHighScoreGui(null);
        }
    }
}
