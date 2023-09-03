package me.ranzeplay.hnation.server.managers.communication;

import me.ranzeplay.hnation.server.db.DbPlayer;
import me.ranzeplay.hnation.server.db.communication.DbSquad;

import java.util.ArrayList;
import java.util.UUID;

public class SquadManager {
    public ArrayList<DbSquad> squads = new ArrayList<>();

    public boolean createSquad(DbPlayer player, DbSquad squad) {
        if(!isPlayerInSquad(player)) {
            return squads.add(squad);
        }

        return false;
    }

    public boolean isPlayerInSquad(DbPlayer player) {
        return squads.stream().anyMatch(s -> s.getMembers().containsKey(player.getId()));
    }

    public DbSquad getLeadingSquad(DbPlayer player) {
        return squads.stream().filter(s -> s.getLeaderId() == player.getId()).findFirst().orElse(null);
    }

    public DbSquad getSquadById(UUID id) {
        squads.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }
}
