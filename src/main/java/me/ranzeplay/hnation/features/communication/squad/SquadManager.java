package me.ranzeplay.hnation.features.communication.squad;

import me.ranzeplay.hnation.features.communication.squad.db.DbSquad;
import me.ranzeplay.hnation.features.communication.squad.tasks.SquadDismissTask;
import me.ranzeplay.hnation.features.player.db.DbPlayer;
import me.ranzeplay.hnation.main.ServerMain;
import net.minecraft.server.network.ServerPlayerEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.UUID;

public class SquadManager {
    public ArrayList<DbSquad> squads;
    public HashMap<UUID, Timer> dismissTasks;

    public SquadManager() {
        squads = new ArrayList<>();
        dismissTasks = new HashMap<>();
    }

    public boolean createSquad(DbPlayer player, DbSquad squad) {
        if (!isPlayerInSquad(player)) {
            return squads.add(squad);
        }

        return false;
    }

    public boolean isPlayerInSquad(DbPlayer player) {
        return squads.stream().anyMatch(s -> s.getMembers().containsKey(player.getId()));
    }

    public DbSquad getLeadingSquad(DbPlayer player) {
        return squads.stream()
                .filter(s -> s.getLeaderId() == player.getId())
                .findFirst()
                .orElse(null);
    }

    public DbSquad getLeadingSquad(ServerPlayerEntity playerEntity) {
        DbPlayer player;
        try {
            player = ServerMain.dbManager
                    .getPlayerDao()
                    .queryForId(playerEntity.getUuid());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return getLeadingSquad(player);
    }

    public DbSquad getPlayerInSquad(DbPlayer player) {
        return squads.stream()
                .filter(s -> s.getMembers().containsKey(player.getId()))
                .findFirst()
                .orElse(null);
    }

    public DbSquad getPlayerInSquad(ServerPlayerEntity playerEntity) throws SQLException {
        var player = ServerMain.dbManager
                .getPlayerDao()
                .queryForId(playerEntity.getUuid());
        return getPlayerInSquad(player);
    }

    public DbSquad getSquadById(UUID id) {
        return squads.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void dismissSquad(UUID id) {
        squads.remove(getSquadById(id));
    }

    public void dismissSquad(UUID squadId, long delay) {
        var task = new SquadDismissTask(squadId);
        var timer = new Timer();
        timer.schedule(task, delay);

        dismissTasks.put(squadId, timer);
    }

    public void cancelDismiss(UUID squadId) {
        dismissTasks.get(squadId).cancel();
    }

    public void joinSquad(UUID squadId, UUID playerId) throws SQLException {
        var squad = getSquadById(squadId);
        var player = ServerMain.dbManager
                .getPlayerDao()
                .queryForId(playerId);

        squad.joinPlayer(player);
    }

    public void transferOwnership(UUID squadId, UUID newLeaderPlayerId) throws SQLException {
        var squad = getSquadById(squadId);
        var newLeader = ServerMain.dbManager
                .getPlayerDao()
                .queryForId(newLeaderPlayerId);

        squad.transferLeader(newLeader);
    }
}
