package me.ranzeplay.hnation.main;

import me.ranzeplay.hnation.db.DatabaseManager;
import me.ranzeplay.hnation.features.communication.messaging.global.server.GlobalChatServerHandler;
import me.ranzeplay.hnation.features.communication.squad.SquadManager;
import me.ranzeplay.hnation.features.communication.squad.server.SquadServerHandler;
import me.ranzeplay.hnation.features.player.server.ServerPlayerHandler;
import me.ranzeplay.hnation.features.poi.server.ServerPOIHandler;
import me.ranzeplay.hnation.features.region.server.ServerRegionHandler;
import me.ranzeplay.hnation.features.transit.server.ServerTransitLineManager;
import net.fabricmc.api.DedicatedServerModInitializer;
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
        ServerPlayerHandler.registerEvents();

        ServerPOIHandler.registerEvents();
        ServerRegionHandler.registerEvents();
        ServerTransitLineManager.registerEvents();

        GlobalChatServerHandler.registerEvents();
        SquadServerHandler.registerEvents();
    }
}
