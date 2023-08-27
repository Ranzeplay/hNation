package me.ranzeplay.hnation.server.db.communication;

import me.ranzeplay.hnation.server.db.DbPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class DbSquad {
    UUID id;
    String name;
    HashMap<UUID, DbPlayer> members;
    ArrayList<DbSquadMessage> messages;
    UUID leaderId;

    public DbSquad(String name, DbPlayer leader) {
        this.name = name;
        id = UUID.randomUUID();
        members = new HashMap<>();
        messages = new ArrayList<>();

        this.joinPlayer(leader);
        leaderId = leader.getId();
    }

    public void joinPlayer(DbPlayer player) {
        members.put(player.getId(), player);
    }

    public void dropPlayer(DbPlayer player) {
        members.remove(player.getId());
    }

    public void dropPlayer(UUID id) {
        members.remove(id);
    }

    public String getName() {
        return name;
    }

    public void sendMessage(DbSquadMessage message) {
        this.messages.add(message);
    }

    public ArrayList<DbSquadMessage> getMessages() {
        return messages;
    }

    public void transferLeader(DbPlayer leader) {
        leaderId = leader.getId();
    }
}
