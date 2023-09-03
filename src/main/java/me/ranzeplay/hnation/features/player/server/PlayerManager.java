package me.ranzeplay.hnation.features.player.server;

import me.ranzeplay.hnation.main.ServerMain;
import me.ranzeplay.hnation.features.player.db.DbPlayer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.sql.SQLException;

public class PlayerManager {
    public static void onPlayerJoin(ServerPlayerEntity player) throws SQLException {
        var dao = ServerMain.dbManager.getPlayerDao();

        DbPlayer dbPlayer = dao.queryForId(player.getUuid());
        if (dbPlayer == null) {
            dbPlayer = new DbPlayer(player.getUuid(), player.getEntityName());
            dao.create(dbPlayer);
        }
    }
}
