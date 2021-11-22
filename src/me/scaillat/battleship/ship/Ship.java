package me.scaillat.battleship.ship;

import me.scaillat.battleship.objects.Location;
import me.scaillat.battleship.objects.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Ship {

    private final ShipType shipType;
    private final List<Location> locationList;
    private Player discoveredShipPlayer;

    protected Ship(ShipType shipType) {
        this.shipType = shipType;
        this.locationList = new ArrayList<>();
        this.discoveredShipPlayer = null;
    }

    public int getSize() {
        return this.shipType.getSize();
    }

    public ShipType getShipType() {
        return shipType;
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public boolean isDiscovered() {
        return this.discoveredShipPlayer != null;
    }

    public Player getDiscoveredShipPlayer() {
        return discoveredShipPlayer;
    }

    public void setDiscoveredShipPlayer(Player discoveredShipPlayer) {
        this.discoveredShipPlayer = discoveredShipPlayer;
    }

    public boolean isSunk() {
        for (Location location : this.locationList) {
            if (!location.isHit()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasAtLeastOneHit() {
        for (Location location : this.locationList) {
            if (location.isHit()) {
                return true;
            }
        }
        return false;
    }
}
