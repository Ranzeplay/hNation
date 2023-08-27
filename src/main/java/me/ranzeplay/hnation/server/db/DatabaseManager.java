package me.ranzeplay.hnation.server.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import me.ranzeplay.hnation.server.db.transit.DbTransitConnector;
import me.ranzeplay.hnation.server.db.transit.DbTransitLine;
import me.ranzeplay.hnation.server.db.transit.DbTransitNode;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseManager {
    final String connectionString;
    ConnectionSource connectionSource;

    Dao<DbPlayer, UUID> playerDao;
    Dao<DbPOI, UUID> poiDao;
    Dao<DbRegion, UUID> regionDao;
    Dao<DbTransitLine, UUID> transitLineDao;
    Dao<DbTransitConnector, UUID> transitConnectorDao;
    Dao<DbTransitNode, UUID> transitNodeDao;

    public DatabaseManager(Path dbFile) throws SQLException {
        connectionString = "jdbc:sqlite:" + dbFile;
        connectionSource = new JdbcConnectionSource(connectionString);

        playerDao = DaoManager.createDao(connectionSource, DbPlayer.class);

        poiDao = DaoManager.createDao(connectionSource, DbPOI.class);
        regionDao = DaoManager.createDao(connectionSource, DbRegion.class);
        transitLineDao = DaoManager.createDao(connectionSource, DbTransitLine.class);
        transitConnectorDao = DaoManager.createDao(connectionSource, DbTransitConnector.class);
        transitNodeDao = DaoManager.createDao(connectionSource, DbTransitNode.class);
    }

    public void init() throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, DbPlayer.class);
        TableUtils.createTableIfNotExists(connectionSource, DbPOI.class);
        TableUtils.createTableIfNotExists(connectionSource, DbRegion.class);
        TableUtils.createTableIfNotExists(connectionSource, DbTransitLine.class);
        TableUtils.createTableIfNotExists(connectionSource, DbTransitNode.class);
        TableUtils.createTableIfNotExists(connectionSource, DbTransitConnector.class);
    }

    public Dao<DbPOI, UUID> getPoiDao() {
        return poiDao;
    }

    public Dao<DbPlayer, UUID> getPlayerDao() {
        return playerDao;
    }

    public Dao<DbTransitLine, UUID> getTransitLineDao() {
        return transitLineDao;
    }

    public Dao<DbTransitConnector, UUID> getTransitConnectorDao() {
        return transitConnectorDao;
    }

    public Dao<DbTransitNode, UUID> getTransitNodeDao() {
        return transitNodeDao;
    }

    public Dao<DbRegion, UUID> getRegionDao() {
        return regionDao;
    }
}
