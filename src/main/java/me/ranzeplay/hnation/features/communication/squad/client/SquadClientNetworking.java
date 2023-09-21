package me.ranzeplay.hnation.features.communication.squad.client;

import me.ranzeplay.hnation.features.communication.squad.viewmodel.SquadCreationReplyViewModel;
import me.ranzeplay.hnation.main.ClientMain;
import me.ranzeplay.hnation.networking.ChatIdentifier;
import me.ranzeplay.hnation.networking.SquadIdentifier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

import java.util.Objects;

public class SquadClientNetworking {
    public static void sendSquadMessage(String message) {
        if (ClientMain.joinedSquad != null) {
            ClientPlayNetworking.send(ChatIdentifier.SEND_CHAT_SQUAD, PacketByteBufs.create().writeString(message));
        }
    }

    public static void receiveMessage(MinecraftClient client, PacketByteBuf packetByteBuf) {
        var name = packetByteBuf.readString();
        var message = packetByteBuf.readString();

        assert client.player != null;
        client.player.sendMessage(Text.of(String.format("<%s @ SQUAD> %s", name, message)));
    }

    public static void createSquadResponse(MinecraftClient client, PacketByteBuf packetByteBuf) {
        var reply = new SquadCreationReplyViewModel(Objects.requireNonNull(packetByteBuf.readNbt()));

        assert client.player != null;
        if (reply.isSuccess()) {
            client.player.sendMessage(Text.of("[SQUAD] Successfully created a squad"));
        } else {
            client.player.sendMessage(Text.of(String.format("[SQUAD] Failed to create squad: %s", reply.getMessage())));
        }
    }

    public static void receiveInvitation(MinecraftClient client, PacketByteBuf packetByteBuf) {
        var inviter = packetByteBuf.readString();
        var squadId = packetByteBuf.readUuid();

        assert client.player != null;
        client.player.sendMessage(Text.literal(String.format("[SQUAD] %s invites you to join their squad", inviter)));
    }

    public static void beingKicked(MinecraftClient client, PacketByteBuf packetByteBuf) {
        var targetPlayerName = packetByteBuf.readString();

        assert client.player != null;
        if (targetPlayerName.equals(client.player.getEntityName())) {
            ClientMain.joinedSquad = null;
            client.player.sendMessage(Text.of("[SQUAD] You are kicked from your squad"));
        } else {
            client.player.sendMessage(Text.of(String.format("[SQUAD] %s is kicked from the squad by leader", targetPlayerName)));
        }
    }

    public static void beingWarned(MinecraftClient client, PacketByteBuf packetByteBuf) {
        var message = packetByteBuf.readString();

        assert client.player != null;
        client.player.sendMessage(Text.of(String.format("[SQUAD WARN] %s", message)));
    }

    public static void beingDismissed(MinecraftClient client) {
        assert client.player != null;
        client.player.sendMessage(Text.of("[SQUAD] Squad has been dismissed"));
        ClientMain.joinedSquad = null;
    }

    public static void ownershipTransfer(MinecraftClient client, PacketByteBuf packetByteBuf) {
        var player = Objects.requireNonNull(client.player);
        var newLeader = packetByteBuf.readString();

        if(newLeader.equals(player.getEntityName())) {
            player.sendMessage(Text.literal("[SQUAD] You are the new leader"));
        } else {
            player.sendMessage(Text.literal(String.format("[SQUAD] New leader is %s", newLeader)));
        }
    }

    public static void newPlayerJoin(MinecraftClient client, PacketByteBuf packetByteBuf) {
        var joinedPlayer = packetByteBuf.readString();

        assert client.player != null;
        client.player.sendMessage(Text.literal(String.format("[SQUAD] %s joined the squad", joinedPlayer)));
    }
    public static void playerLeave(MinecraftClient client, PacketByteBuf packetByteBuf) {
        var leftPlayer = packetByteBuf.readString();

        assert client.player != null;
        client.player.sendMessage(Text.literal(String.format("[SQUAD] %s left the squad", leftPlayer)));
    }

    public static void registerEvents() {
        ClientPlayNetworking.registerGlobalReceiver(SquadIdentifier.SQUAD_MESSAGE_NOTIFY,
                (minecraftClient, _clientPlayNetworkHandler, packetByteBuf, _packetSender)
                        -> receiveMessage(minecraftClient, packetByteBuf)
        );

        ClientPlayNetworking.registerGlobalReceiver(SquadIdentifier.SQUAD_KICK_NOTIFY,
                (minecraftClient, _clientPlayNetworkHandler, packetByteBuf, _packetSender)
                        -> beingKicked(minecraftClient, packetByteBuf)
        );

        ClientPlayNetworking.registerGlobalReceiver(SquadIdentifier.SQUAD_DISMISS_NOTIFY,
                (minecraftClient, _clientPlayNetworkHandler, _packetByteBuf, _packetSender)
                        -> beingDismissed(minecraftClient)
        );

        ClientPlayNetworking.registerGlobalReceiver(SquadIdentifier.SQUAD_INVITE_NOTIFY,
                (minecraftClient, _clientPlayNetworkHandler, packetByteBuf, _packetSender)
                        -> receiveInvitation(minecraftClient, packetByteBuf)
        );

        ClientPlayNetworking.registerGlobalReceiver(SquadIdentifier.SQUAD_WARN_NOTIFY,
                (minecraftClient, _clientPlayNetworkHandler, packetByteBuf, _packetSender)
                        -> beingWarned(minecraftClient, packetByteBuf)
        );

        ClientPlayNetworking.registerGlobalReceiver(SquadIdentifier.SQUAD_CREATE_REPLY,
                (minecraftClient, _clientPlayNetworkHandler, packetByteBuf, _packetSender)
                        -> createSquadResponse(minecraftClient, packetByteBuf)
        );

        ClientPlayNetworking.registerGlobalReceiver(SquadIdentifier.SQUAD_TRANSFER_NOTIFY,
                (minecraftClient, _clientPlayNetworkHandler, packetByteBuf, _packetSender)
                        -> ownershipTransfer(minecraftClient, packetByteBuf)
        );

        ClientPlayNetworking.registerGlobalReceiver(SquadIdentifier.SQUAD_LEAVE_NOTIFY,
                (minecraftClient, _clientPlayNetworkHandler, packetByteBuf, _packetSender)
                        -> playerLeave(minecraftClient, packetByteBuf)
        );
    }
}
