package me.ranzeplay.hnation.features.communication.squad.client;

import me.ranzeplay.hnation.main.ClientMain;
import me.ranzeplay.hnation.networking.ChatIdentifier;
import me.ranzeplay.hnation.networking.SquadIdentifier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class SquadClientNetworking {
    public static void sendSquad(String message) {
        if (ClientMain.joinedSquad != null) {
            ClientPlayNetworking.send(ChatIdentifier.SEND_CHAT_SQUAD, PacketByteBufs.create().writeString(message));
        }
    }

    public static void receiveMessage(MinecraftClient client, PacketByteBuf packetByteBuf) {
        var name = packetByteBuf.readString();
        var message = packetByteBuf.readString();

        assert client.player != null;
        client.player.sendMessage(Text.of(String.format("[%s @ SQUAD] %s", name, message)));
    }

    public static void registerEvents() {
        ClientPlayNetworking.registerGlobalReceiver(SquadIdentifier.SQUAD_MESSAGE_NOTIFY,
                (minecraftClient, _clientPlayNetworkHandler, packetByteBuf, packetSender) -> receiveMessage(minecraftClient, packetByteBuf)
        );
    }
}
