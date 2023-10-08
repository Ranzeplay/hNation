package me.ranzeplay.hnation.features.transit;

import com.j256.ormlite.dao.Dao;
import me.ranzeplay.hnation.features.transit.db.DbTransitLine;

import java.sql.SQLException;
import java.util.UUID;

public class TransitLineManager {
    private static TransitLineManager Instance;

    final Dao<DbTransitLine, UUID> dbContext;

    public TransitLineManager(Dao<DbTransitLine, UUID> dbContext) {
        this.dbContext = dbContext;
        Instance = this;
    }

    public DbTransitLine getTransitLine(UUID id) {
        try {
            return dbContext.queryForId(id);
        } catch (SQLException e) {
            return null;
        }
    }

    public DbTransitLine getTransitLine(String name) {
        try {
            var query = dbContext.queryBuilder().where()
                    .eq("name", name)
                    .prepare();
            return dbContext.queryForFirst(query);
        } catch (SQLException e) {
            return null;
        }
    }

    public static TransitLineManager getInstance() {
        return Instance;
    }
}
