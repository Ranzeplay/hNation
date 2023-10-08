package me.ranzeplay.hnation.features.region;

import com.j256.ormlite.dao.Dao;
import me.ranzeplay.hnation.features.region.db.DbRegion;

import java.sql.SQLException;
import java.util.UUID;

public class RegionManager {
    private static RegionManager Instance;
    private final Dao<DbRegion, UUID> dbContext;

    public RegionManager(Dao<DbRegion, UUID> dao) {
        Instance = this;
        dbContext = dao;
    }

    public static RegionManager getInstance() {
        return Instance;
    }

    public DbRegion getRegion(UUID id) {
        try {
            return dbContext.queryForId(id);
        } catch (SQLException e) {
            return null;
        }
    }

    public DbRegion getRegion(String name) {
        try {
            var query = dbContext.queryBuilder().where()
                    .eq("name", name)
                    .prepare();
            return dbContext.queryForFirst(query);
        } catch (SQLException e) {
            return null;
        }
    }
}
