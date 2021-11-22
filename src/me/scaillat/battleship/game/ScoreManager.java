package me.scaillat.battleship.game;

import me.scaillat.battleship.objects.HighScore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

public class ScoreManager {

    private final TreeSet<HighScore> highScores;
    private ScoreType scoreType;

    public ScoreManager() {
        this.highScores = new TreeSet<>();
        this.loadHighScores();
        this.scoreType = ScoreType.REGULAR;
    }

    public SortedSet<HighScore> getHighScores() {
        return this.highScores;
    }

    public void addHighScore(String playerName, int highScore) {
        this.highScores.add(new HighScore(playerName, highScore));
    }

    public void resetHighScore() {
        this.highScores.clear();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void saveHighScores() {
        File file = new File("highscores.txt");

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write("Username,High Score\n");

            for (HighScore highScore : this.getHighScores()) {
                fileWriter.write(highScore.getUsername() + "," + highScore.getValue() + "\n");
            }

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadHighScores() {
        File file = new File("highscores.txt");
        if (!file.exists()) {
            return;
        }

        try {
            Scanner scanner = new Scanner(file);
            scanner.nextLine(); // skip the first line because of header

            String[] line;
            String username;
            int score;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine().split(",");
                if (line.length >= 2) {
                    username = line[0];
                    score = Integer.parseInt(line[1]);

                    this.getHighScores().add(new HighScore(username, score));
                }
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ScoreType getScoreType() {
        return this.scoreType;
    }

    public void setScoreType(ScoreType scoreType) {
        this.scoreType = scoreType;
    }
}
