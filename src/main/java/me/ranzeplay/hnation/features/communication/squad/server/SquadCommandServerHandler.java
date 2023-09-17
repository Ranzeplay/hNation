package me.ranzeplay.hnation.features.communication.squad.server;

import me.ranzeplay.hnation.features.communication.squad.db.DbSquad;
import me.ranzeplay.hnation.features.communication.squad.viewmodel.SquadCreationReplyViewModel;
import me.ranzeplay.hnation.main.NetworkingIdentifier;
import me.ranzeplay.hnation.main.ServerMain;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.sql.SQLException;

public class SquadCommandServerHandler {
    public static void createSquad(ServerPlayerEntity sender, PacketByteBuf packetByteBuf) throws SQLException {
        var nbt = packetByteBuf.readNbt();
        assert nbt != null;
        var squad = new DbSquad(nbt);
        var player = ServerMain.dbManager.getPlayerDao().queryForId(sender.getUuid());
        var success = ServerMain.squadManager.createSquad(player, squad);
        if (success) {
            ServerPlayNetworking.send(sender, NetworkingIdentifier.SQUAD_CREATE_REPLY,
                    PacketByteBufs.create().writeNbt(new SquadCreationReplyViewModel(true, "").toNbt()));
        } else {
            ServerPlayNetworking.send(sender, NetworkingIdentifier.SQUAD_CREATE_REPLY,
                    PacketByteBufs.create().writeNbt(new SquadCreationReplyViewModel(false, "Failed to create a squad").toNbt()));
        }
    }

    public static void invitePlayerToSquad(MinecraftServer server, ServerPlayerEntity sender, PacketByteBuf packetByteBuf) throws SQLException {
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
        var squad = ServerMain.squadManager.getLeadingSquad(sender);
        if (squad != null) {
            var targetPlayer = server.getPlayerManager().getPlayer(targetDbPlayer.getId());
            if (!squad.getInvitations().containsKey(targetDbPlayer.getId()) && targetPlayer != null) {
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
        if (squad != null) {
            if (!squad.getJoinRequests().containsKey(player.getId())) {
                squad.invitePlayer(player.getId());

                sender.sendMessage(Text.literal("Request has been sent"));
            } else if (squad.getInvitations().containsKey(player.getId())) {
                ServerMain.squadManager.joinSquad(squadId, player.getId());
            }
        }
    }

    public static void playerSendMessage(MinecraftServer server, ServerPlayerEntity sender, PacketByteBuf packetByteBuf) throws SQLException {
        var squad = ServerMain.squadManager.getPlayerInSquad(sender);
        var message = packetByteBuf.readString();

        squad.getMembers().forEach((u, p) -> {
            var pn = server.getPlayerManager().getPlayer(u);
            assert pn != null;
            ServerPlayNetworking.send(pn, NetworkingIdentifier.SQUAD_MESSAGE_NOTIFY, PacketByteBufs.create().writeUuid(sender.getUuid()).writeString(message));
        });
    }

    public static void playerLeaveSquad(MinecraftServer server, ServerPlayerEntity sender) throws SQLException {
        var player = ServerMain.dbManager
                .getPlayerDao()
                .queryForId(sender.getUuid());
        var squad = ServerMain.squadManager.getPlayerInSquad(player);
        squad.dropPlayer(player);

        // Notify each player of the leave event
        squad.getMembers().forEach((u, p) -> {
            var pn = server.getPlayerManager().getPlayer(u);
            assert pn != null;
            ServerPlayNetworking.send(pn, NetworkingIdentifier.SQUAD_LEAVE_NOTIFY, PacketByteBufs.create().writeString(player.getName()));
        });
    }

    public static void kickPlayer(MinecraftServer server, ServerPlayerEntity sender, PacketByteBuf packetByteBuf) {
        var squad = ServerMain.squadManager.getLeadingSquad(sender);
        var target = server.getPlayerManager().getPlayer(packetByteBuf.readString());
        assert target != null;

        // Notify each player of the kick event
        squad.getMembers().forEach((u, p) -> {
            var player = server.getPlayerManager().getPlayer(u);
            assert player != null;
            ServerPlayNetworking.send(player, NetworkingIdentifier.SQUAD_KICK_NOTIFY, PacketByteBufs.create().writeString(target.getEntityName()));
        });

        squad.dropPlayer(target.getUuid());
    }

    public static void warnPlayer(MinecraftServer server, ServerPlayerEntity sender, PacketByteBuf packetByteBuf) {
        var target = server.getPlayerManager().getPlayer(packetByteBuf.readString());
        var reason = packetByteBuf.readString();
        var squad = ServerMain.squadManager.getLeadingSquad(sender);
        if (squad != null) {
            assert target != null;
            ServerPlayNetworking.send(target, NetworkingIdentifier.SQUAD_WARN_NOTIFY, PacketByteBufs.create().writeString(reason));
        }
    }

    public static void registerEvents() {
        ServerPlayNetworking.registerGlobalReceiver(NetworkingIdentifier.SQUAD_CREATE_REQUEST,
                (_minecraftServer, sender, _serverPlayNetworkHandler, packetByteBuf, _packetSender) -> {
                    try {
                        SquadCommandServerHandler.createSquad(sender, packetByteBuf);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(NetworkingIdentifier.SQUAD_INVITE_REQUEST,
                (minecraftServer, sender, _serverPlayNetworkHandler, packetByteBuf, _packetSender) -> {
                    try {
                        SquadCommandServerHandler.invitePlayerToSquad(minecraftServer, sender, packetByteBuf);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(NetworkingIdentifier.SQUAD_KICK_REQUEST,
                (minecraftServer, sender, _serverPlayNetworkHandler, packetByteBuf, _packetSender) -> {
                    SquadCommandServerHandler.kickPlayer(minecraftServer, sender, packetByteBuf);
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(NetworkingIdentifier.SQUAD_JOIN_REQUEST,
                (_minecraftServer, sender, _serverPlayNetworkHandler, packetByteBuf, _packetSender) -> {
                    try {
                        SquadCommandServerHandler.playerJoinSquadRequest(sender, packetByteBuf);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(NetworkingIdentifier.SQUAD_LEAVE_REQUEST,
                (minecraftServer, sender, _serverPlayNetworkHandler, _packetByteBuf, _packetSender) -> {
                    try {
                        SquadCommandServerHandler.playerLeaveSquad(minecraftServer, sender);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(NetworkingIdentifier.SQUAD_WARN_REQUEST,
                (minecraftServer, sender, _serverPlayNetworkHandler, packetByteBuf, _packetSender) -> {
                    SquadCommandServerHandler.warnPlayer(minecraftServer, sender, packetByteBuf);
                }
        );
    }
}
