package me.ranzeplay.hnation.features.transit;

import com.j256.ormlite.dao.Dao;
import me.ranzeplay.hnation.features.transit.db.DbTransitNode;

import java.sql.SQLException;
import java.util.UUID;

public class TransitNodeManager {
    private static TransitNodeManager INSTANCE;

    final Dao<DbTransitNode, UUID> dbContext;

    public TransitNodeManager(Dao<DbTransitNode, UUID> dbContext) {
        this.dbContext = dbContext;
        INSTANCE = this;
    }

    public void create(DbTransitNode node) {
        try {
            dbContext.create(node);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DbTransitNode getNode(UUID uuid) {
        try {
            return dbContext.queryForId(uuid);
        } catch (SQLException e) {
            return null;
        }
    }

    public DbTransitNode getNode(String name) {
        try {
            var query = dbContext.queryBuilder().where()
                    .eq("name", name)
                    .prepare();
            return dbContext.queryForFirst(query);
        } catch (SQLException e) {
            return null;
        }
    }

    public static TransitNodeManager getInstance() {
        return INSTANCE;
    }
}
