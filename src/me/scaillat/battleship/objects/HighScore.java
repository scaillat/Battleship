package me.scaillat.battleship.objects;

import java.util.Comparator;

public class HighScore implements Comparable<HighScore> {

    private final String username;
    private final int value;

    public HighScore(String username, int value) {
        this.username = username;
        this.value = value;
    }

    public String getUsername() {
        return username;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(HighScore o) {
        return Comparator.comparing(HighScore::getValue)
                .thenComparing(HighScore::getUsername)
                .compare(o, this);
    }

    @Override
    public String toString() {
        return "HighScore{" +
                "username='" + username + '\'' +
                ", value=" + value +
                '}';
    }
}
