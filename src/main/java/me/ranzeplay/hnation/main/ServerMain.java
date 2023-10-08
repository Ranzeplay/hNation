package me.ranzeplay.hnation.main;

import me.ranzeplay.hnation.db.DatabaseManager;
import me.ranzeplay.hnation.features.communication.announcement.AnnouncementManager;
import me.ranzeplay.hnation.features.communication.announcement.server.AnnouncementCommandServerHandler;
import me.ranzeplay.hnation.features.communication.announcement.server.ServerAnnouncementNetworking;
import me.ranzeplay.hnation.features.communication.messaging.direct.server.ServerDirectMessageNetworking;
import me.ranzeplay.hnation.features.communication.messaging.global.server.ServerGlobalChatNetworking;
import me.ranzeplay.hnation.features.communication.squad.SquadManager;
import me.ranzeplay.hnation.features.communication.squad.server.SquadCommandServerHandler;
import me.ranzeplay.hnation.features.player.PlayerManager;
import me.ranzeplay.hnation.features.player.server.ServerPlayerNetworking;
import me.ranzeplay.hnation.features.poi.server.ServerPOIHandler;
import me.ranzeplay.hnation.features.region.server.ServerRegionNetworking;
import me.ranzeplay.hnation.features.transit.TransitLineManager;
import me.ranzeplay.hnation.features.transit.TransitNodeManager;
import me.ranzeplay.hnation.features.transit.server.ServerTransitLineNetworking;
import net.fabricmc.api.DedicatedServerModInitializer;
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
            if(configDirectory.toFile().mkdirs()) {
                GeneralMain.LOGGER.info("Created config directory");
            }
        }

        try {
            Path dbFilePath = Paths.get(configDirectory.toAbsolutePath().toString(), "data.db");
            dbManager = new DatabaseManager(dbFilePath);
            dbManager.init();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        new PlayerManager(dbManager.getPlayerDao());
        new SquadManager();
        new AnnouncementManager(dbManager.getAnnouncementDao());
        new TransitLineManager(dbManager.getTransitLineDao());
        new TransitNodeManager(dbManager.getTransitNodeDao());
    }

    private void registerNetworkingHandlers() {
        ServerPlayerNetworking.registerEvents();

        ServerPOIHandler.registerEvents();
        ServerRegionNetworking.registerEvents();
        ServerTransitLineNetworking.registerEvents();

        ServerGlobalChatNetworking.registerEvents();
        ServerDirectMessageNetworking.registerEvents();
        SquadCommandServerHandler.registerEvents();

        AnnouncementCommandServerHandler.registerEvents();
        ServerAnnouncementNetworking.registerEvents();
    }
}
