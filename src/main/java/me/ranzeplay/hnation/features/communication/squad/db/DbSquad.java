package me.ranzeplay.hnation.features.communication.squad.db;

import me.ranzeplay.hnation.features.player.db.DbPlayer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DbSquad {
    UUID id;
    HashMap<UUID, DbPlayer> members;
    ArrayList<DbSquadMessage> messages;
    UUID leaderId;
    Map<UUID, Long> joinRequests;
    Map<UUID, Long> invitations;

    public DbSquad(DbPlayer leader) {
        id = UUID.randomUUID();
        members = new HashMap<>();
        messages = new ArrayList<>();
        joinRequests = new HashMap<>();
        invitations = new HashMap<>();

        this.joinPlayer(leader);
        leaderId = leader.getId();
    }

    public void joinPlayer(DbPlayer player) {
        members.put(player.getId(), player);

        joinRequests.remove(player.getId());
        invitations.remove(player.getId());
    }

    public void dropPlayer(DbPlayer player) {
        members.remove(player.getId());
    }

    public void dropPlayer(UUID id) {
        members.remove(id);
    }


    public void sendMessage(DbSquadMessage message) {
        this.messages.add(message);
    }

    public ArrayList<DbSquadMessage> getMessages() {
        return messages;
    }

    public boolean transferLeader(DbPlayer leader) {
        if (members.containsValue(leader)) {
            leaderId = leader.getId();
            return true;
        }

        return false;
    }

    public void invitePlayer(UUID playerId) {
        invitations.put(playerId, System.currentTimeMillis());
    }

    public void removeInvitation(UUID playerId) {
        invitations.remove(playerId);
    }

    public void addJoinRequest(UUID playerId) {
        joinRequests.put(playerId, System.currentTimeMillis());
    }

    public void removeJoinRequest(UUID playerId) {
        joinRequests.remove(playerId);
    }

    public NbtCompound toNbt() {
        var nbt = new NbtCompound();
        nbt.putUuid("id", id);
        nbt.putUuid("leaderId", leaderId);

        var invitationNbtList = new NbtList();
        invitations.forEach((id, time) -> {
            var comp = new NbtCompound();
            comp.putUuid("playerId", id);
            comp.putLong("time", time);
            invitationNbtList.add(comp);
        });
        nbt.put("invitations", invitationNbtList);

        var joinRequestList = new NbtList();
        joinRequests.forEach((id, time) -> {
            var comp = new NbtCompound();
            comp.putUuid("playerId", id);
            comp.putLong("time", time);
            invitationNbtList.add(comp);
        });
        nbt.put("joinRequests", joinRequestList);

        var messageList = new NbtList();
        messages.forEach((msg) -> {
            invitationNbtList.add(msg.toNbt());
        });
        nbt.put("messages", messageList);

        var memberList = new NbtList();
        members.forEach((id, player) -> {
            var comp = new NbtCompound();
            comp.putUuid("id", id);
            comp.put("player", player.toNbt());
            memberList.add(comp);
        });
        nbt.put("messages", memberList);

        return nbt;
    }

    public DbSquad(NbtCompound nbt) {
        id = nbt.getUuid("id");
        leaderId = nbt.getUuid("leaderId");

        members = new HashMap<>();
        var memberList = nbt.getList("members", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < memberList.size(); i++) {
            var comp = memberList.getCompound(i);
            members.put(comp.getUuid("id"), new DbPlayer(comp.getCompound("player")));
        }

        messages = new ArrayList<>();
        var messageList = nbt.getList("messages", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < messageList.size(); i++) {
            var comp = messageList.getCompound(i);
            messages.add(new DbSquadMessage(comp));
        }

        joinRequests = new HashMap<>();
        var joinRequestList = nbt.getList("joinRequests", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < joinRequestList.size(); i++) {
            var comp = memberList.getCompound(i);
            joinRequests.put(comp.getUuid("playerId"), nbt.getLong("time"));
        }

        invitations = new HashMap<>();
        var invitationList = nbt.getList("invitations", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < invitationList.size(); i++) {
            var comp = memberList.getCompound(i);
            invitations.put(comp.getUuid("playerId"), nbt.getLong("time"));
        }
    }

    public HashMap<UUID, DbPlayer> getMembers() {
        return members;
    }

    public UUID getId() {
        return id;
    }

    public UUID getLeaderId() {
        return leaderId;
    }

    public Map<UUID, Long> getJoinRequests() {
        return joinRequests;
    }

    public Map<UUID, Long> getInvitations() {
        return invitations;
    }
}
