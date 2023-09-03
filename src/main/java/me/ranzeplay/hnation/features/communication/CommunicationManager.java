package me.ranzeplay.hnation.features.communication;

import me.ranzeplay.hnation.main.ClientMain;
import me.ranzeplay.hnation.main.NetworkingIdentifier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

public class CommunicationManager {
    public static void handle(String message) {
        switch(ClientMain.communicationFocusOption) {

            case CHANNEL -> {
            }
            case PUBLIC -> sendPublic(message);
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
