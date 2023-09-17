package me.ranzeplay.hnation.features.communication.messaging.global.client;

import me.ranzeplay.hnation.networking.ChatIdentifier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

public class ClientGlobalMessageNetworking {
    public static void sendPublic(String message) {
        ClientPlayNetworking.send(ChatIdentifier.SEND_CHAT_PUBLIC, PacketByteBufs.create().writeString(message));
    }
}
