package me.scaillat.battleship;

import me.scaillat.battleship.game.Game;

public class BattleShipGame {

    public static void main(String[] args) {
        Game game = new Game();
        game.open();

        // On close
        Runtime.getRuntime().addShutdownHook(new Thread(() -> game.getScoreManager().saveHighScores()));
    }
}
