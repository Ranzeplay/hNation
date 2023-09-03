package me.ranzeplay.hnation.features.player.server;

import me.ranzeplay.hnation.main.ServerMain;
import me.ranzeplay.hnation.features.player.db.DbPlayer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;

import java.sql.SQLException;

public class ServerPlayerHandler {
    public static void onPlayerJoin(ServerPlayerEntity player) throws SQLException {
        var dao = ServerMain.dbManager.getPlayerDao();

        DbPlayer dbPlayer = dao.queryForId(player.getUuid());
        if (dbPlayer == null) {
            dbPlayer = new DbPlayer(player.getUuid(), player.getEntityName());
            dao.create(dbPlayer);
        }
    }

    public static void registerEvents() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            try {
                ServerPlayerHandler.onPlayerJoin(handler.getPlayer());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
