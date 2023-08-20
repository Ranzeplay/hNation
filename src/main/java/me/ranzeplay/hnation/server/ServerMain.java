package me.ranzeplay.hnation.server;

import me.ranzeplay.hnation.networking.NetworkingIdentifier;
import me.ranzeplay.hnation.server.db.DatabaseManager;
import me.ranzeplay.hnation.server.networking.POIOperation;
import net.fabricmc.api.DedicatedServerModInitializer;
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
    }
}
