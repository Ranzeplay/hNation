package me.ranzeplay.hnation.features.communication.announcement;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import me.ranzeplay.hnation.features.communication.announcement.db.DbAnnouncement;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

public class AnnouncementManager {
    private static AnnouncementManager INSTANCE = null;

    public static AnnouncementManager getInstance() {
        return INSTANCE;
    }

    final Dao<DbAnnouncement, UUID> dbContext;

    public AnnouncementManager(Dao<DbAnnouncement, UUID> dbContext) {
        this.dbContext = dbContext;
        INSTANCE = this;
    }

    public void create(DbAnnouncement announcement) {
        try {
            dbContext.create(announcement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DbAnnouncement getById(UUID id) {
        try {
            return dbContext.queryForId(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<DbAnnouncement> getLatest(long count) {
        try {
            var query = dbContext.queryBuilder().orderBy("createTime", false).limit(count).prepare();
            return new ArrayList<>(dbContext.query(query));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<DbAnnouncement> getByTime(Timestamp fromTime, Timestamp toTime) {
        return new ArrayList<>();
    }
}
