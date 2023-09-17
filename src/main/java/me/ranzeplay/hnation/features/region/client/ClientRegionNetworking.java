package me.ranzeplay.hnation.features.region.client;

import me.ranzeplay.hnation.networking.RegionIdentifier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientRegionNetworking {
    public static void registerEvents() {
        ClientPlayNetworking.registerGlobalReceiver(RegionIdentifier.CREATE_REGION_REPLY,
                (minecraftClient, _clientPlayNetworkHandler, _packetByteBuf, _packetSender) -> ClientRegionCommand.commitCreationReply(minecraftClient)
        );
    }
}
