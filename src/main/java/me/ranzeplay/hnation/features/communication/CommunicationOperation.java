package me.ranzeplay.hnation.features.communication;

import me.ranzeplay.hnation.main.NetworkingIdentifier;
import me.ranzeplay.hnation.features.communication.squad.viewmodel.SquadCreationReplyViewModel;
import me.ranzeplay.hnation.main.ServerMain;
import me.ranzeplay.hnation.features.communication.squad.db.DbSquad;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.sql.SQLException;

public class CommunicationOperation {

    public static void createSquad(ServerPlayerEntity sender, PacketByteBuf packetByteBuf) throws SQLException {
        var nbt = packetByteBuf.readNbt();
        assert nbt != null;
        var squad = new DbSquad(nbt);
        var player = ServerMain.dbManager.getPlayerDao().queryForId(sender.getUuid());
        var success = ServerMain.squadManager.createSquad(player, squad);
        if(success) {
            ServerPlayNetworking.send(sender, NetworkingIdentifier.SQUAD_CREATE_REPLY,
                    PacketByteBufs.create().writeNbt(new SquadCreationReplyViewModel(true, "").toNbt()));
        }else {
            ServerPlayNetworking.send(sender, NetworkingIdentifier.SQUAD_CREATE_REPLY,
                    PacketByteBufs.create().writeNbt(new SquadCreationReplyViewModel(false, "Failed to create a squad").toNbt()));
        }
    }

    public static void invitePlayerToSquad(MinecraftServer server, ServerPlayerEntity sender, PacketByteBuf packetByteBuf) throws SQLException {
        var player = ServerMain.dbManager
                .getPlayerDao()
                .queryForId(sender.getUuid());
        var targetPlayerName = packetByteBuf.readString();
        var targetDbPlayer = ServerMain.dbManager
                .getPlayerDao()
                .queryForFirst(ServerMain.dbManager.getPlayerDao()
                        .queryBuilder()
                        .where()
                        .eq("name", targetPlayerName)
                        .prepare()
                );
        // Check if player is the leader of a squad
        var squad = ServerMain.squadManager.getLeadingSquad(player);
        if(squad != null) {
            var targetPlayer = server.getPlayerManager().getPlayer(targetDbPlayer.getId());
            if(!squad.getInvitations().containsKey(targetDbPlayer.getId()) && targetPlayer != null) {
                squad.invitePlayer(targetDbPlayer.getId());

                targetPlayer.sendMessage(Text.literal("You are being invited to " + targetDbPlayer.getName() + "'s squad"));
            }
        }
    }

    public static void playerJoinSquadRequest(ServerPlayerEntity sender, PacketByteBuf packetByteBuf) throws SQLException {
        var player = ServerMain.dbManager
                .getPlayerDao()
                .queryForId(sender.getUuid());
        var squadId = packetByteBuf.readUuid();
        var squad = ServerMain.squadManager.getSquadById(squadId);
        if(squad != null) {
            if(!squad.getJoinRequests().containsKey(player.getId())) {
                squad.invitePlayer(player.getId());

                sender.sendMessage(Text.literal("Request has been sent"));
            }
        }
    }
}
