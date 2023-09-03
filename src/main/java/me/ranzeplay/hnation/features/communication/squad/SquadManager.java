package me.ranzeplay.hnation.features.communication.squad;

import me.ranzeplay.hnation.features.communication.squad.db.DbSquad;
import me.ranzeplay.hnation.features.player.db.DbPlayer;

import java.util.ArrayList;
import java.util.UUID;

public class SquadManager {
    public ArrayList<DbSquad> squads;

    public SquadManager() {
        squads = new ArrayList<DbSquad>();
    }

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
        return squads.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }
}
