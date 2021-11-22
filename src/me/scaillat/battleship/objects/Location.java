package me.scaillat.battleship.objects;

import me.scaillat.battleship.ship.Ship;

public class Location {

    private final int x;
    private final int y;
    private Status status;
    private Ship ship;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;

        this.status = Status.UNKNOWN;
        this.ship = null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isUnknown() {
        return this.status == Status.UNKNOWN;
    }

    public boolean isHit() {
        return this.status == Status.HIT;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public boolean isShip() {
        return this.ship != null;
    }

}
