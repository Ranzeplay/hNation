package me.ranzeplay.hnation.features.region.client;

import me.ranzeplay.hnation.main.NetworkingIdentifier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientRegionHandler {
    public static void registerEvents() {
        ClientPlayNetworking.registerGlobalReceiver(NetworkingIdentifier.CREATE_REGION_REPLY,
                (minecraftClient, _clientPlayNetworkHandler, _packetByteBuf, _packetSender) -> ClientRegionCommand.commitCreationReply(minecraftClient)
        );
    }
}
