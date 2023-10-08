package me.ranzeplay.hnation.features.player;

import com.j256.ormlite.dao.Dao;
import me.ranzeplay.hnation.features.player.db.DbPlayer;
import me.ranzeplay.hnation.main.ServerMain;
import net.minecraft.server.network.ServerPlayerEntity;

import java.sql.SQLException;
import java.util.UUID;

public class PlayerManager {
    private static PlayerManager INSTANCE = null;

    final Dao<DbPlayer, UUID> playerDao;

    public PlayerManager(Dao<DbPlayer, UUID> playerDao) {
        this.playerDao = playerDao;
        INSTANCE = this;
    }

    public static PlayerManager getInstance() {
        return INSTANCE;
    }

    public DbPlayer getPlayer(UUID playerId) {
        try {
            return playerDao.queryForId(playerId);
        } catch (SQLException e) {
            return null;
        }
    }

    public DbPlayer getPlayer(String playerName) {
        try {
            return playerDao.queryForFirst(ServerMain.dbManager.getPlayerDao()
                    .queryBuilder()
                    .where()
                    .eq("name", playerName)
                    .prepare()
            );
        } catch (SQLException e) {
            return null;
        }
    }

    public DbPlayer getPlayer(ServerPlayerEntity playerEntity) {
        try {
            return playerDao.queryForId(playerEntity.getUuid());
        } catch (SQLException e) {
            return null;
        }
    }

    public DbPlayer createIfNotExists(ServerPlayerEntity playerEntity) {
        try {
            var dbPlayer = playerDao.queryForId(playerEntity.getUuid());
            if (dbPlayer == null) {
                dbPlayer = new DbPlayer(playerEntity.getUuid(), playerEntity.getEntityName());
                playerDao.create(dbPlayer);
                return dbPlayer;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
