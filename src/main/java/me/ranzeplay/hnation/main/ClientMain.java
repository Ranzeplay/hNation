package me.ranzeplay.hnation.main;

import me.ranzeplay.hnation.features.communication.CommunicationFocusOption;
import me.ranzeplay.hnation.features.communication.channel.client.ClientChannelCommand;
import me.ranzeplay.hnation.features.communication.squad.client.SquadCommandClientHandler;
import me.ranzeplay.hnation.features.communication.squad.db.DbSquad;
import me.ranzeplay.hnation.features.poi.client.ClientPoiCommand;
import me.ranzeplay.hnation.features.poi.client.ClientPoiNetworking;
import me.ranzeplay.hnation.features.region.client.ClientRegionCommand;
import me.ranzeplay.hnation.features.region.client.ClientRegionNetworking;
import me.ranzeplay.hnation.features.transit.client.ClientTransitCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

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
                    .then(ClientPoiCommand.buildCommandTree())
                    .then(ClientRegionCommand.buildCommandTree())
                    .then(ClientChannelCommand.buildCommandTree())
                    .then(ClientTransitCommand.buildCommandTree())
                    .then(SquadCommandClientHandler.buildCommandTree())
            );
        });
    }

    private void registerNetworkingHandlers() {
        ClientPoiNetworking.registerEvents();
        ClientRegionNetworking.registerEvents();
    }
}
