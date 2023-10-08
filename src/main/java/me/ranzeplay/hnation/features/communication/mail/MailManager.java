package me.ranzeplay.hnation.features.communication.mail;

import com.j256.ormlite.dao.Dao;
import me.ranzeplay.hnation.features.communication.mail.db.DbMail;
import me.ranzeplay.hnation.features.communication.mail.db.MailStatus;
import me.ranzeplay.hnation.features.player.db.DbPlayer;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class MailManager {
    final Dao<DbMail, UUID> dbContext;

    public MailManager(Dao<DbMail, UUID> dbContext) {
        this.dbContext = dbContext;
    }

    public void sendMail(DbMail mail) {
        try {
            dbContext.create(mail);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DbMail> getInbox(DbPlayer player) {
        try {
            var query = dbContext.queryBuilder()
                    .orderBy("sendTime", true)
                    .where()
                    .eq("receiver", player)
                    .eq("status", MailStatus.UNREAD)
                    .prepare();
            return dbContext.query(query);
        } catch (SQLException e) {
            return null;
        }
    }

    public DbMail getById(UUID id) {
        try {
            return dbContext.queryForId(id);
        } catch (SQLException e) {
            return null;
        }
    }

    public List<DbMail> getLatest(DbPlayer player, long count) {
        try {
            var query = dbContext.queryBuilder()
                    .orderBy("sendTime", true)
                    .limit(count)
                    .where()
                    .eq("receiver", player)
                    .or()
                    .eq("sender", player)
                    .prepare();
            return dbContext.query(query);
        } catch (SQLException e) {
            return null;
        }
    }
}
