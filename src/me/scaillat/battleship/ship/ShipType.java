package me.scaillat.battleship.ship;

public enum ShipType {
    DESTROYER(2, 400),
    SUBMARINE(3, 300),
    BATTLESHIP(4, 200),
    CARRIER(5, 100);

    private final int size;
    private final int hitValue;

    ShipType(int size, int hitValue) {
        this.size = size;
        this.hitValue = hitValue;
    }

    public int getSize() {
        return size;
    }

    public int getHitValue() {
        return hitValue;
    }

    public static boolean isShipType(String shipType) {
        try {
            ShipType.valueOf(shipType.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
