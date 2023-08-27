package me.ranzeplay.hnation.client;

import me.ranzeplay.hnation.networking.NetworkingIdentifier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

public class CommunicationManager {
    public static void handle(String message) {
        switch(ClientMain.chatFocus) {

            case CHANNEL -> {
            }
            case PUBLIC -> {
                sendPublic(message);
            }
            case PRIVATE -> {
            }
            case SQUAD -> {
            }
        }
    }

    private static void sendPublic(String message) {
        ClientPlayNetworking.send(NetworkingIdentifier.SEND_CHAT_PUBLIC, PacketByteBufs.create().writeString(message));
    }
}
