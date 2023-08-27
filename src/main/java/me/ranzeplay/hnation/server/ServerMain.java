package me.ranzeplay.hnation.server;

import me.ranzeplay.hnation.networking.NetworkingIdentifier;
import me.ranzeplay.hnation.server.db.DatabaseManager;
import me.ranzeplay.hnation.server.networking.CommunicationOperation;
import me.ranzeplay.hnation.server.networking.POIOperation;
import me.ranzeplay.hnation.server.networking.RegionOperation;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public class ServerMain implements DedicatedServerModInitializer {
    public static DatabaseManager dbManager = null;

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
                        CommunicationOperation.publicChat(minecraftServer, sender, packetByteBuf);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            try {
                PlayerManager.onPlayerJoin(handler.getPlayer());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
