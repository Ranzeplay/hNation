package me.ranzeplay.hnation.features.communication.messaging.direct.client;

import me.ranzeplay.hnation.features.communication.messaging.direct.viewmodel.DirectMessageView;
import me.ranzeplay.hnation.networking.ChatIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class ClientDirectMessageNetworking {
    public static void registerEvents(){
        ClientPlayNetworking.registerGlobalReceiver(ChatIdentifier.RECEIVE_CHAT_DIRECT,
                (minecraftClient, _clientPlayNetworkHandler, packetByteBuf, _packetSender)
                        -> receiveMessage(minecraftClient, packetByteBuf)
        );
    }

    public static void sendDirect(String targetPlayerName, String message) {
        var messageView = new DirectMessageView(message, Objects.requireNonNull(MinecraftClient.getInstance().player).getEntityName(), targetPlayerName);
        ClientPlayNetworking.send(ChatIdentifier.SEND_CHAT_DIRECT, PacketByteBufs.create().writeNbt(messageView.toNbt()));
        MinecraftClient.getInstance().player.sendMessage(messageView.toMessageText());
    }

    private static void receiveMessage(MinecraftClient client, PacketByteBuf packetByteBuf) {
        var messageView = new DirectMessageView(Objects.requireNonNull(packetByteBuf.readNbt()));

        if (client.player != null) {
            client.player.sendMessage(messageView.toMessageText());
        }
    }
}
