package me.ranzeplay.hnation.features.communication.squad.server;

import me.ranzeplay.hnation.features.communication.squad.SquadManager;
import me.ranzeplay.hnation.features.communication.squad.db.DbSquad;
import me.ranzeplay.hnation.features.communication.squad.viewmodel.SquadCreationReplyViewModel;
import me.ranzeplay.hnation.features.player.PlayerManager;
import me.ranzeplay.hnation.networking.ChatIdentifier;
import me.ranzeplay.hnation.networking.SquadIdentifier;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SquadCommandServerHandler {
    public static void createSquad(ServerPlayerEntity sender, PacketByteBuf packetByteBuf) {
        var nbt = packetByteBuf.readNbt();
        assert nbt != null;
        var squad = new DbSquad(nbt);
        var player = PlayerManager.getInstance().getPlayer(sender);
        var success = SquadManager.getInstance().createSquad(player, squad);
        if (success) {
            ServerPlayNetworking.send(sender, SquadIdentifier.SQUAD_CREATE_REPLY,
                    PacketByteBufs.create().writeNbt(new SquadCreationReplyViewModel(true, "").toNbt()));
        } else {
            ServerPlayNetworking.send(sender, SquadIdentifier.SQUAD_CREATE_REPLY,
                    PacketByteBufs.create().writeNbt(new SquadCreationReplyViewModel(false, "Failed to create a squad").toNbt()));
        }
    }

    public static void invitePlayerToSquad(MinecraftServer server, ServerPlayerEntity sender, PacketByteBuf packetByteBuf) {
        var targetDbPlayer = PlayerManager.getInstance().getPlayer(packetByteBuf.readString());
        // Check if player is the leader of a squad
        var squad = SquadManager.getInstance().getLeadingSquad(sender);
        if (squad != null) {
            var targetPlayer = server.getPlayerManager().getPlayer(targetDbPlayer.getId());
            if (!squad.getInvitations().containsKey(targetDbPlayer.getId()) && targetPlayer != null) {
                squad.invitePlayer(targetDbPlayer.getId());

                targetPlayer.sendMessage(Text.literal("You are being invited to " + targetDbPlayer.getName() + "'s squad"));
            }
        }
    }

    public static void playerJoinSquadRequest(ServerPlayerEntity sender, PacketByteBuf packetByteBuf) {
        var player = PlayerManager.getInstance().getPlayer(sender);
        var squadId = packetByteBuf.readUuid();
        var squad = SquadManager.getInstance().getSquadById(squadId);
        if (squad != null) {
            if (!squad.getJoinRequests().containsKey(player.getId())) {
                squad.invitePlayer(player.getId());

                sender.sendMessage(Text.literal("Request has been sent"));
            } else if (squad.getInvitations().containsKey(player.getId())) {
                SquadManager.getInstance().joinSquad(squadId, player.getId());
            }
        }
    }

    public static void playerSendMessage(MinecraftServer server, ServerPlayerEntity sender, PacketByteBuf packetByteBuf) {
        var squad = SquadManager.getInstance().getPlayerInSquad(sender);
        var message = packetByteBuf.readString();
        var player = PlayerManager.getInstance().getPlayer(sender);

        squad.getMembers().forEach((u, p) -> {
            var pn = server.getPlayerManager().getPlayer(u);
            assert pn != null;
            ServerPlayNetworking.send(pn, SquadIdentifier.SQUAD_MESSAGE_NOTIFY, PacketByteBufs.create().writeString(player.getName()).writeString(message));
        });
    }

    public static void playerLeaveSquad(MinecraftServer server, ServerPlayerEntity sender) {
        var player = PlayerManager.getInstance().getPlayer(sender);
        var squad = SquadManager.getInstance().getPlayerInSquad(player);
        squad.dropPlayer(player);

        // Notify each player of the leave event
        squad.getMembers().forEach((u, p) -> {
            var pn = server.getPlayerManager().getPlayer(u);
            assert pn != null;
            ServerPlayNetworking.send(pn, SquadIdentifier.SQUAD_LEAVE_NOTIFY, PacketByteBufs.create().writeString(player.getName()));
        });
    }

    public static void kickPlayer(MinecraftServer server, ServerPlayerEntity sender, PacketByteBuf packetByteBuf) {
        var squad = SquadManager.getInstance().getLeadingSquad(sender);
        var target = PlayerManager.getInstance().getPlayer(packetByteBuf.readString());
        assert target != null;

        // Notify each player of the kick event
        squad.getMembers().forEach((u, p) -> {
            var player = server.getPlayerManager().getPlayer(u);
            assert player != null;
            ServerPlayNetworking.send(player, SquadIdentifier.SQUAD_KICK_NOTIFY, PacketByteBufs.create().writeString(target.getName()));
        });

        squad.dropPlayer(target.getId());
    }

    public static void warnPlayer(MinecraftServer server, ServerPlayerEntity sender, PacketByteBuf packetByteBuf) {
        var target = server.getPlayerManager().getPlayer(packetByteBuf.readString());
        var reason = packetByteBuf.readString();
        var squad = SquadManager.getInstance().getLeadingSquad(sender);
        if (squad != null) {
            assert target != null;
            ServerPlayNetworking.send(target, SquadIdentifier.SQUAD_WARN_NOTIFY, PacketByteBufs.create().writeString(reason));
        }
    }

    public static void transferOwnership(MinecraftServer server, ServerPlayerEntity sender, PacketByteBuf packetByteBuf) {
        var squad = SquadManager.getInstance().getLeadingSquad(sender);
        var targetPlayer = PlayerManager.getInstance().getPlayer(packetByteBuf.readString());
        squad.transferLeader(targetPlayer);

        squad.getMembers().forEach((u, p) -> {
            var player = server.getPlayerManager().getPlayer(u);
            assert player != null;
            ServerPlayNetworking.send(player, SquadIdentifier.SQUAD_TRANSFER_NOTIFY, PacketByteBufs.create().writeString(targetPlayer.getName()));
        });
    }

    public static void dismiss(MinecraftServer server, ServerPlayerEntity sender) {
        var squad = SquadManager.getInstance().getLeadingSquad(sender);
        squad.getMembers().forEach((u, p) -> {
            var player = server.getPlayerManager().getPlayer(u);
            assert player != null;
            ServerPlayNetworking.send(player, SquadIdentifier.SQUAD_DISMISS_NOTIFY, PacketByteBufs.create());
        });
    }

    public static void registerEvents() {
        ServerPlayNetworking.registerGlobalReceiver(SquadIdentifier.SQUAD_CREATE_REQUEST,
                (_minecraftServer, sender, _serverPlayNetworkHandler, packetByteBuf, _packetSender) -> {
                    SquadCommandServerHandler.createSquad(sender, packetByteBuf);
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(SquadIdentifier.SQUAD_INVITE_REQUEST,
                (minecraftServer, sender, _serverPlayNetworkHandler, packetByteBuf, _packetSender) -> {
                    SquadCommandServerHandler.invitePlayerToSquad(minecraftServer, sender, packetByteBuf);
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(SquadIdentifier.SQUAD_KICK_REQUEST,
                (minecraftServer, sender, _serverPlayNetworkHandler, packetByteBuf, _packetSender) -> {
                    SquadCommandServerHandler.kickPlayer(minecraftServer, sender, packetByteBuf);
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(SquadIdentifier.SQUAD_JOIN_REQUEST,
                (_minecraftServer, sender, _serverPlayNetworkHandler, packetByteBuf, _packetSender) -> {
                    SquadCommandServerHandler.playerJoinSquadRequest(sender, packetByteBuf);
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(SquadIdentifier.SQUAD_LEAVE_REQUEST,
                (minecraftServer, sender, _serverPlayNetworkHandler, _packetByteBuf, _packetSender) -> {
                    SquadCommandServerHandler.playerLeaveSquad(minecraftServer, sender);
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(SquadIdentifier.SQUAD_WARN_REQUEST,
                (minecraftServer, sender, _serverPlayNetworkHandler, packetByteBuf, _packetSender) -> {
                    SquadCommandServerHandler.warnPlayer(minecraftServer, sender, packetByteBuf);
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(ChatIdentifier.SEND_CHAT_SQUAD,
                (minecraftServer, sender, _serverPlayNetworkHandler, packetByteBuf, _packetSender) -> {
                    SquadCommandServerHandler.playerSendMessage(minecraftServer, sender, packetByteBuf);
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(SquadIdentifier.SQUAD_DISMISS_REQUEST,
                (minecraftServer, sender, _serverPlayNetworkHandler, _packetByteBuf, _packetSender) -> {
                    SquadCommandServerHandler.dismiss(minecraftServer, sender);
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(SquadIdentifier.SQUAD_TRANSFER_REQUEST,
                (minecraftServer, sender, _serverPlayNetworkHandler, packetByteBuf, _packetSender) -> {
                    SquadCommandServerHandler.transferOwnership(minecraftServer, sender, packetByteBuf);
                }
        );
    }
}
