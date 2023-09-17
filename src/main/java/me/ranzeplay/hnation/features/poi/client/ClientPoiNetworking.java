package me.ranzeplay.hnation.features.poi.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import static me.ranzeplay.hnation.networking.PoiIdentifier.QUERY_POI_REPLY;

public class ClientPoiNetworking {
    public static void registerEvents() {
        ClientPlayNetworking.registerGlobalReceiver(QUERY_POI_REPLY,
                (minecraftClient, _clientPlayNetworkHandler, packetByteBuf, packetSender) -> ClientPoiCommand.queryReply(minecraftClient, packetByteBuf)
        );
    }
}
