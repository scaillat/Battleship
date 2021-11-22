package me.scaillat.battleship.objects;

import javax.swing.*;

public class Player {

    private final String userName;
    private int score;
    private final JLabel scoreLabel;

    public Player(String userName) {
        this.userName = userName;
        this.score = 0;
        this.scoreLabel = new JLabel();
    }

    public String getUserName() {
        return this.userName;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
        this.scoreLabel.setText(String.valueOf(this.getScore()));
    }

    public void addScore(int score) {
        this.setScore(this.getScore() + score);
        this.scoreLabel.setText(String.valueOf(this.getScore()));
    }

    public JLabel getScoreLabel() {
        return this.scoreLabel;
    }
}
