package me.ranzeplay.hnation.features.communication.squad;

import me.ranzeplay.hnation.features.communication.squad.db.DbSquad;
import me.ranzeplay.hnation.features.communication.squad.tasks.SquadDismissTask;
import me.ranzeplay.hnation.features.player.PlayerManager;
import me.ranzeplay.hnation.features.player.db.DbPlayer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.UUID;

public class SquadManager {
    private static SquadManager INSTANCE = null;

    public ArrayList<DbSquad> squads;
    public HashMap<UUID, Timer> dismissTasks;

    public SquadManager() {
        squads = new ArrayList<>();
        dismissTasks = new HashMap<>();
        INSTANCE = this;
    }

    public static SquadManager getInstance() {
        return INSTANCE;
    }

    public boolean createSquad(DbPlayer leader, DbSquad squad) {
        if (!isPlayerInSquad(leader)) {
            return squads.add(squad);
        }

        return false;
    }

    public boolean isPlayerInSquad(DbPlayer player) {
        return squads.stream()
                .anyMatch(s -> s.getMembers().containsKey(player.getId()));
    }

    public DbSquad getLeadingSquad(DbPlayer player) {
        return squads.stream()
                .filter(s -> s.getLeaderId() == player.getId())
                .findFirst()
                .orElse(null);
    }

    public DbSquad getLeadingSquad(ServerPlayerEntity playerEntity) {
        DbPlayer player;
        player = PlayerManager.getInstance().getPlayer(playerEntity);
        return getLeadingSquad(player);
    }

    public DbSquad getPlayerInSquad(DbPlayer player) {
        return squads.stream()
                .filter(s -> s.getMembers().containsKey(player.getId()))
                .findFirst()
                .orElse(null);
    }

    public DbSquad getPlayerInSquad(ServerPlayerEntity playerEntity) {
        var player = PlayerManager.getInstance().getPlayer(playerEntity);
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

    public void joinSquad(UUID squadId, UUID playerId) {
        var squad = getSquadById(squadId);
        var player = PlayerManager.getInstance().getPlayer(playerId);

        squad.joinPlayer(player);
    }

    public void transferOwnership(UUID squadId, UUID newLeaderPlayerId) {
        var squad = getSquadById(squadId);
        var newLeader = PlayerManager.getInstance().getPlayer(newLeaderPlayerId);

        squad.transferLeader(newLeader);
    }
}
