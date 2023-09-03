package me.ranzeplay.hnation.features.poi.client;

import me.ranzeplay.hnation.main.NetworkingIdentifier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientPOIHandler {
    public static void registerEvents() {
        ClientPlayNetworking.registerGlobalReceiver(NetworkingIdentifier.QUERY_POI_REPLY,
                (minecraftClient, _clientPlayNetworkHandler, packetByteBuf, packetSender) -> POICommand.queryReply(minecraftClient, packetByteBuf)
        );
    }
}
