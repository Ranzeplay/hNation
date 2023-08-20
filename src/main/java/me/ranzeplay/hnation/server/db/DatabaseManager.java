package me.ranzeplay.hnation.server.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseManager {
    final String connectionString;
    ConnectionSource connectionSource;

    Dao<DbPOI, UUID> poiDao;

    public DatabaseManager(Path dbFile) throws SQLException {
        connectionString = "jdbc:sqlite:" + dbFile;
        connectionSource = new JdbcConnectionSource(connectionString);

        poiDao = DaoManager.createDao(connectionSource, DbPOI.class);
    }

    public void init() throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, DbPOI.class);
    }

    public Dao<DbPOI, UUID> getPoiDao() {
        return poiDao;
    }
}
