package me.ranzeplay.hnation.features.communication.messaging.direct.server;

import me.ranzeplay.hnation.features.communication.messaging.direct.viewmodel.DirectMessageView;
import me.ranzeplay.hnation.features.player.PlayerManager;
import me.ranzeplay.hnation.networking.ChatIdentifier;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;

import java.util.Objects;

public class ServerDirectMessageNetworking {
    public static void registerEvents() {
        ServerPlayNetworking.registerGlobalReceiver(ChatIdentifier.SEND_CHAT_DIRECT,
                (minecraftServer, _sender, _serverPlayNetworkHandler, packetByteBuf, _packetSender) -> {
                    transmit(minecraftServer, packetByteBuf);
                }
        );
    }

    private static void transmit(MinecraftServer server, PacketByteBuf packetByteBuf) {
        var viewModel = new DirectMessageView(Objects.requireNonNull(packetByteBuf.readNbt()));
        var target = PlayerManager.getInstance().getPlayer(viewModel.getReceiverName());
        var entity = server.getPlayerManager().getPlayer(target.getId());

        ServerPlayNetworking.send(Objects.requireNonNull(entity), ChatIdentifier.RECEIVE_CHAT_DIRECT, PacketByteBufs.create().writeNbt(viewModel.toNbt()));
    }
}
