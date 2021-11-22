package me.scaillat.battleship.game;

import me.scaillat.battleship.objects.Location;
import me.scaillat.battleship.ship.*;
import me.scaillat.battleship.game.gui.GameGui;
import me.scaillat.battleship.objects.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class GameBoard {

    private static final List<String> RANDOM_PLAYER_NAMES = Arrays.asList("Captain", "Bob", "Apple", "Guy", "Ghost", "Cat", "Hywel", "Jackson", "Scott", "Chicago");

    private final HashMap<Integer, Location> positionLocationMap;
    private final Game game;
    private final List<Ship> shipList;
    private final List<Player> players;
    private Player currentPlayer;
    private int lastPlayer;
    private File gameFile;
    private Location[][] board;
    private GameGui gameGui;

    public GameBoard(Game game) {
        this.positionLocationMap = new HashMap<>();
        this.game = game;

        this.gameFile = null;
        this.shipList = new ArrayList<>();
        this.gameGui = null;
        this.currentPlayer = null;

        this.players = new ArrayList<>();
        this.generatePlayers();
    }

    public Location[][] getBoard() {
        return this.board;
    }

    public GameGui getGameGui() {
        return this.gameGui;
    }

    public Player getPlayer1() {
        if (this.players.isEmpty()) {
            return null;
        }
        return this.players.get(0);
    }

    public Player getPlayer2() {
        if (this.players.size() < 2) {
            return null;
        }

        return this.players.get(1);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void changePlayer() {
        if (this.lastPlayer == 0) {
            this.lastPlayer = 1;
        } else {
            this.lastPlayer = 0;
        }

        this.currentPlayer = this.players.get(lastPlayer);
    }

    public void generatePlayers() {
        this.players.clear();
        this.players.add(new Player(getRandomPlayerName() + getRandomPlayerTag()));
        this.players.add(new Player(getRandomPlayerName() + getRandomPlayerTag()));
    }

    public void setGameFile(File gameFile) {
        this.gameFile = gameFile;
    }

    public String loadGameBoard() {
        if (this.gameFile == null) {
            return "You must select a game file first!";
        }

        try {
            // Read and Cache File
            Scanner scanner = new Scanner(this.gameFile);

            int linesLength = 5;
            String[] lines = new String[linesLength];
            int i = 0;
            while (scanner.hasNextLine()) {
                if (i >= linesLength) break;
                lines[i] = scanner.nextLine();
                ++i;
            }
            scanner.close();

            // Load size
            int boardSize;
            if (!isNumber(lines[0])) {
                System.err.println("The first line of the game file isn't a number! Using default 8 x 8 size instead...");
                boardSize = 8;
            } else {
                boardSize = Integer.parseInt(lines[0]);
            }
            this.board = new Location[boardSize][boardSize];

            int position = 0;
            for (int x = 0; x < boardSize; ++x) {
                for (int y = 0; y < boardSize; ++y) {
                    this.board[x][y] = new Location(x, y);
                    this.positionLocationMap.put(position, this.board[x][y]);

                    position++;
                }
            }

            // Load ships
            for (int j = 1; j < linesLength; ++j) {
                String line = lines[j];
                if (line == null) {
                    return "There are missing ships in your game file! Please update!";
                }

                String[] shipInfo = line.split(";");
                if (shipInfo.length < 2) {
                    return "There are missing arguments for ships in your game file! Please update!";
                }

                if (!ShipType.isShipType(shipInfo[0])) {
                    return "\"" + shipInfo[0] + "\"is not a valid ship type! Please update!";
                }

                ShipType shipType = ShipType.valueOf(shipInfo[0].toUpperCase());
                if (shipType.getSize() != shipInfo.length - 1) {
                    return "The size of the " + shipType + " is incorrect!" +
                            " Found: " + (shipInfo.length - 1) + " but expected: " + shipType.getSize() + " Please update!";
                }

                String msg = this.createShip(shipType, shipInfo);
                if (!msg.equals("")) {
                    return msg;
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            return "An error occurred! (FileNotFoundException)";
        }

        this.generatePlayers();
        return "";
    }

    public void open() {
        this.lastPlayer = this.getRandomBetween(0, 1);
        this.currentPlayer = this.players.get(lastPlayer);
        this.gameGui = new GameGui(this.game);
    }

    public Location getCoordsFromPosition(int position) {
        return positionLocationMap.get(position);
    }

    private String createShip(ShipType shipType, String[] shipInfo) {
        Ship ship;
        switch (shipType) {
            case CARRIER:
                ship = new Carrier();
                return this.placeShip(ship, shipInfo);
            case BATTLESHIP:
                ship = new BattleShip();
                return this.placeShip(ship, shipInfo);
            case DESTROYER:
                ship = new Destroyer();
                return this.placeShip(ship, shipInfo);
            case SUBMARINE:
                ship = new Submarine();
                return this.placeShip(ship, shipInfo);
            default:
                throw new IllegalStateException("Unexpected value: " + shipType);
        }
    }

    private String placeShip(Ship ship, String[] shipInfo) {
        Location location;
        for (int i = 1; i < shipInfo.length; ++i) {
            String[] coords = shipInfo[i].split("\\*");

            if (!isNumber(coords[0])) {
                return "An error occurred whilst trying to load " + ship.getShipType() + " \"" + coords[0] + "\" is not a valid x coordinate!";
            }
            if (!isNumber(coords[1])) {
                return "An error occurred whilst trying to load " + ship.getShipType() + " \"" + coords[1] + "\" is not a valid y coordinate!";
            }

            int x = Integer.parseInt(coords[0]) - 1;
            int y = Integer.parseInt(coords[1]) - 1;

            if (x < 0 || x >= board.length || y < 0 || y >= board.length) {
                return "The coordinates (" + (x + 1) + "," + (y + 1) + ") are outside the board. Please update!";
            }

            location = this.board[x][y];
            if (location.isShip()) {
                return "The coordinates (" + (x + 1) + "," + (y + 1) + ") already contain a ship. Please update!";
            }

            location.setShip(ship);

            ship.getLocationList().add(location);
            this.shipList.add(ship);
        }

        return "";
    }

    private String getRandomPlayerName() {
        String randomName = RANDOM_PLAYER_NAMES.get(getRandomBetween(0, RANDOM_PLAYER_NAMES.size() - 1));
        if (getPlayer1() != null && getPlayer1().getUserName().equals(randomName) || getPlayer2() != null && getPlayer2().getUserName().equals(randomName)) {
            return getRandomPlayerName();
        }
        return randomName;
    }

    private String getRandomPlayerTag() {
        return "#" + getRandomBetween(1, 9) + getRandomBetween(1, 9) + getRandomBetween(1, 9) + getRandomBetween(1, 9);
    }

    private boolean isNumber(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int getRandomBetween(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }
}
