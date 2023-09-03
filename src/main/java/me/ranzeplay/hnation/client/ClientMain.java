package me.ranzeplay.hnation.client;

import me.ranzeplay.hnation.client.commands.POICommand;
import me.ranzeplay.hnation.client.commands.RegionCommand;
import me.ranzeplay.hnation.client.commands.comm.ChannelCommand;
import me.ranzeplay.hnation.client.commands.transit.TransitCommand;
import me.ranzeplay.hnation.networking.NetworkingIdentifier;
import me.ranzeplay.hnation.server.db.DbPlayer;
import me.ranzeplay.hnation.server.db.communication.DbSquad;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Environment(EnvType.CLIENT)
public class ClientMain implements ClientModInitializer {
    public static ChatFocus chatFocus = ChatFocus.PUBLIC;
    public static DbSquad joinedSquad = null;

    @Override
    public void onInitializeClient() {
        registerCommands();
        registerNetworkingHandlers();
    }

    private void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("hnt")
                    .then(POICommand.buildCommandTree())
                    .then(RegionCommand.buildCommandTree())
                    .then(ChannelCommand.buildCommandTree())
                    .then(TransitCommand.buildCommandTree())
            );
        });
    }

    private void registerNetworkingHandlers() {
        ClientPlayNetworking.registerGlobalReceiver(NetworkingIdentifier.QUERY_POI_REPLY,
                (minecraftClient, _clientPlayNetworkHandler, packetByteBuf, packetSender) -> POICommand.queryReply(minecraftClient, packetByteBuf)
        );

        ClientPlayNetworking.registerGlobalReceiver(NetworkingIdentifier.CREATE_REGION_REPLY,
                (minecraftClient, _clientPlayNetworkHandler, _packetByteBuf, _packetSender) -> RegionCommand.commitCreationReply(minecraftClient)
        );
    }
}
