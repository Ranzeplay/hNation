package me.ranzeplay.hnation.features.communication.messaging.global.client;

import me.ranzeplay.hnation.main.NetworkingIdentifier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

public class ClientGlobalMessageNetworking {
    public static void sendPublic(String message) {
        ClientPlayNetworking.send(NetworkingIdentifier.SEND_CHAT_PUBLIC, PacketByteBufs.create().writeString(message));
    }
}
