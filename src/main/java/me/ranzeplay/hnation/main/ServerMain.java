package me.ranzeplay.hnation.main;

import me.ranzeplay.hnation.features.communication.messaging.global.server.GlobalChatServerHandler;
import me.ranzeplay.hnation.features.player.server.PlayerManager;
import me.ranzeplay.hnation.db.DatabaseManager;
import me.ranzeplay.hnation.features.transit.server.RailwayManager;
import me.ranzeplay.hnation.features.communication.squad.SquadManager;
import me.ranzeplay.hnation.features.communication.CommunicationOperation;
import me.ranzeplay.hnation.features.poi.server.POIOperation;
import me.ranzeplay.hnation.features.region.server.RegionOperation;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public class ServerMain implements DedicatedServerModInitializer {
    public static DatabaseManager dbManager = null;

    public static SquadManager squadManager = null;

    @Override
    public void onInitializeServer() {
        registerNetworkingHandlers();

        var configDirectory = Paths.get(FabricLoader.getInstance().getConfigDir().toAbsolutePath().toString(), "hNation");
        if (!configDirectory.toFile().exists()) {
            configDirectory.toFile().mkdirs();
        }

        try {
            Path dbFilePath = Paths.get(configDirectory.toAbsolutePath().toString(), "data.db");
            dbManager = new DatabaseManager(dbFilePath);
            dbManager.init();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        squadManager = new SquadManager();
    }

    private void registerNetworkingHandlers() {
        ServerPlayNetworking.registerGlobalReceiver(NetworkingIdentifier.CREATE_POI_REQUEST,
                (_minecraftServer, sender, _serverPlayNetworkHandler, packetByteBuf, _packetSender) -> {
                    try {
                        POIOperation.create(sender, packetByteBuf);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(NetworkingIdentifier.QUERY_POI_REQUEST,
                (_minecraftServer, sender, _serverPlayNetworkHandler, packetByteBuf, _packetSender) -> {
                    POIOperation.query(sender);
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(NetworkingIdentifier.CREATE_REGION_REQUEST,
                (_minecraftServer, sender, _serverPlayNetworkHandler, packetByteBuf, _packetSender) -> {
                    try {
                        RegionOperation.create(sender, packetByteBuf);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(NetworkingIdentifier.SEND_CHAT_PUBLIC,
                (minecraftServer, sender, _serverPlayNetworkHandler, packetByteBuf, _packetSender) -> {
                    try {
                        GlobalChatServerHandler.send(minecraftServer, sender, packetByteBuf);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(NetworkingIdentifier.CREATE_TRANSIT_LINE_REQUEST,
                (_minecraftServer, sender, _serverPlayNetworkHandler, packetByteBuf, _packetSender) -> {
                    RailwayManager.scanRailwayPath(sender, packetByteBuf);
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(NetworkingIdentifier.SQUAD_CREATE_REQUEST,
                (_minecraftServer, sender, _serverPlayNetworkHandler, packetByteBuf, _packetSender) -> {
                    try {
                        CommunicationOperation.createSquad(sender, packetByteBuf);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            try {
                PlayerManager.onPlayerJoin(handler.getPlayer());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
