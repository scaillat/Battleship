package me.scaillat.battleship.game;

import me.scaillat.battleship.game.gui.MainGameGui;
import me.scaillat.battleship.game.gui.GameGui;
import me.scaillat.battleship.objects.Player;

import java.io.File;

public class Game {

    private MainGameGui mainGameGui;
    private final GameBoard gameBoard;
    private final ScoreManager scoreManager;

    public Game() {
        this.scoreManager = new ScoreManager();
        this.gameBoard = new GameBoard(this);
    }

    public MainGameGui getMainGameGui() {
        return mainGameGui;
    }

    public GameBoard getGameBoard() {
        return this.gameBoard;
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    public Player getPlayer1() {
        return this.getGameBoard().getPlayer1();
    }

    public Player getPlayer2() {
        return this.getGameBoard().getPlayer2();
    }

    public Player getCurrentPlayer() {
        return this.getGameBoard().getCurrentPlayer();
    }

    public void loadFile(File file) {
        this.getGameBoard().setGameFile(file);
    }

    public void open() {
        this.mainGameGui = new MainGameGui(this);
    }

    public String validateGameFile() {
        return this.getGameBoard().loadGameBoard();
    }

    public GameGui start() {
        this.getGameBoard().open();
        return gameBoard.getGameGui();
    }
}
