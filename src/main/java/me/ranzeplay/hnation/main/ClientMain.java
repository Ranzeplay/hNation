package me.ranzeplay.hnation.main;

import me.ranzeplay.hnation.features.communication.CommunicationFocusOption;
import me.ranzeplay.hnation.features.poi.client.ClientPOIHandler;
import me.ranzeplay.hnation.features.poi.client.POICommand;
import me.ranzeplay.hnation.features.region.client.ClientRegionCommand;
import me.ranzeplay.hnation.features.communication.channel.client.ChannelCommand;
import me.ranzeplay.hnation.features.region.client.ClientRegionHandler;
import me.ranzeplay.hnation.features.transit.client.TransitCommand;
import me.ranzeplay.hnation.features.communication.squad.db.DbSquad;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Environment(EnvType.CLIENT)
public class ClientMain implements ClientModInitializer {
    public static CommunicationFocusOption communicationFocusOption = CommunicationFocusOption.PUBLIC;
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
                    .then(ClientRegionCommand.buildCommandTree())
                    .then(ChannelCommand.buildCommandTree())
                    .then(TransitCommand.buildCommandTree())
            );
        });
    }

    private void registerNetworkingHandlers() {
        ClientPOIHandler.registerEvents();
        ClientRegionHandler.registerEvents();
    }
}
