package me.scaillat.battleship.game.gui;

import me.scaillat.battleship.game.Game;
import me.scaillat.battleship.game.ScoreType;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainGameGui extends JFrame {

    private final Game game;

    private RulesGui rulesGui;
    private HighScoreGui highScoreGui;
    private GameGui gameGui;

    public MainGameGui(Game game) throws HeadlessException {
        this.game = game;

        this.setTitle("Battleship - Main Menu");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(600, 300);

        this.getContentPane().add(BorderLayout.NORTH, getHeader());
        this.getContentPane().add(BorderLayout.CENTER, getCenterContent());
        this.getContentPane().add(BorderLayout.SOUTH, getFooter());
        this.setVisible(true);
    }

    private Box getHeader() {
        Box headerBox = Box.createVerticalBox();

        // TITLE
        JLabel title = new JLabel("Welcome to Battleship");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Dialog", Font.BOLD, 16));
        headerBox.add(title);

        // SUBTITLE
        JLabel subtitle = new JLabel("Please select your options to get started!");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(new Font("Dialog", Font.ITALIC, 12));
        headerBox.add(subtitle);

        return headerBox;
    }

    private JPanel getCenterContent() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        Box mainButtonBox = Box.createVerticalBox();

        // START GAME
        JButton startButton = new JButton("Start Game");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameGui != null) return;

                String msg = game.validateGameFile();
                if (!msg.equals("")) {
                    JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                gameGui = game.start();
            }
        });
        mainButtonBox.add(startButton);

        JButton shipPlacementButton = new JButton("Upload Game File");
        shipPlacementButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        shipPlacementButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                int returnValue = jFileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    game.loadFile(jFileChooser.getSelectedFile());
                }
            }
        });
        mainButtonBox.add(shipPlacementButton);

        JRadioButton regularScoreButton = new JRadioButton("Regular Score System", true);
        regularScoreButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JRadioButton alternativeScoreButton = new JRadioButton("Alternative Score System", false);
        alternativeScoreButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        regularScoreButton.addActionListener(e -> {
            alternativeScoreButton.setSelected(false);
            this.game.getScoreManager().setScoreType(ScoreType.REGULAR);
        });
        alternativeScoreButton.addActionListener(e -> {
            regularScoreButton.setSelected(false);
            this.game.getScoreManager().setScoreType(ScoreType.ALTERNATIVE);
        });

        mainButtonBox.add(regularScoreButton);
        mainButtonBox.add(alternativeScoreButton);

        centerPanel.add(BorderLayout.CENTER, mainButtonBox);
        return centerPanel;
    }

    private JPanel getFooter() {
        JPanel panel = new JPanel(new FlowLayout());

        // RULES BUTTON
        JButton rulesButton = new JButton("Rules");
        rulesButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rulesGui == null) {
                    rulesGui = new RulesGui(game);
                }
            }
        });

        // HIGH SCORES BUTTON
        JButton highScoresButton = new JButton("High Scores");
        highScoresButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (highScoreGui == null) {
                    highScoreGui = new HighScoreGui(game);
                }
            }
        });

        // EXIT BUTTON
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        panel.add(rulesButton);
        panel.add(highScoresButton);
        panel.add(exitButton);

        return panel;
    }

    public void setRulesGui(RulesGui rulesGui) {
        this.rulesGui = rulesGui;
    }

    public void setHighScoreGui(HighScoreGui highScoreGui) {
        this.highScoreGui = highScoreGui;
    }

    public void setGameGui(GameGui gameGui) {
        this.gameGui = gameGui;
    }
}
