package model.application;

import java.util.Arrays;

import model.exception.FillException;
import model.exception.InvalidSigningException;

public class Team {
    public static int REQUIRED_TEAM_SIZE = 5;

    private final String localName;
    private final String teamName;
    private Manager manager;
    private final Players allPlayers;
    private final Player[] currentTeam;

    public Team(String localName, String teamName, Manager manager, Players allPlayers) {
        this.localName = localName;
        this.teamName = teamName;
        this.manager = manager;
        this.allPlayers = allPlayers;
        this.currentTeam = new Player[REQUIRED_TEAM_SIZE];
    }

    public String getTeamName() {
        return this.teamName;
    }

    public Manager getManager() {
        return this.manager;
    }

    public Players getAllPlayers() { return this.allPlayers; }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Player[] getCurrentTeam(){
        return this.currentTeam;
    }

    public boolean isInCurrentTeam(Player player) {
        for (Player p : currentTeam){
            if (p == player) return true;
        }
        return false;
    }

    public int getCurrentTeamSlot(Player player) {
        for (int i = 0; i < currentTeam.length; i++) {
            if (currentTeam[i] != null && currentTeam[i].equals(player)) {
                return i;
            }
        }
        return -1;
    }

    public void signPlayer(Player player) throws InvalidSigningException {
        if (player == null) {
            throw new InvalidSigningException("Player does not exist within the league");
        }
        if (player.getTeam() == this) {
            throw new InvalidSigningException(player.getFullName() + " is already signed to your team");
        }
        if (player.getTeam() != null) {
            throw new InvalidSigningException("Cannot sign " + player.getFullName() + ", player is already signed to " + player.getTeam().toString());
        }
        player.setTeam(this);
        allPlayers.add(player);
    }
    
    public void unsignPlayer(Player player) throws InvalidSigningException {
        if (isInCurrentTeam(player)) {
            throw new InvalidSigningException("Cannot remove " + player.getFullName() + ", player is in the active team");
        }
        
        player.setTeam(null);
        allPlayers.remove(player);
    }

    public void assignPlayerToSlot(Player player, int slotIndex) throws FillException {
        if (player.getTeam() != this) {
            return; 
        }

        int currentSlot = getCurrentTeamSlot(player);
        Player existingPlayerInTargetSlot = currentTeam[slotIndex];

        if (currentSlot != -1 && currentSlot != slotIndex) {
            throw new FillException(player.getFullName() + " is already in the active playing team");
        }
        
        if (currentSlot == slotIndex) {
            return;
        }
        
        if (existingPlayerInTargetSlot != null) {
        }
        
        currentTeam[slotIndex] = player;
    }
    
    public Player removePlayerFromSlot(int slotIndex) {
        Player removedPlayer = currentTeam[slotIndex];
        if (removedPlayer != null) {
            currentTeam[slotIndex] = null;
        }
        return removedPlayer;
    }

    @Override
    public String toString() {
        return this.localName + " " + this.teamName;
    }
}
