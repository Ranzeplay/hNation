package me.ranzeplay.hnation.features.transit.db;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import me.ranzeplay.hnation.features.region.db.DbRegion;

import java.sql.Timestamp;
import java.util.UUID;

@DatabaseTable(tableName = "transitNode")
public class DbTransitNode {
    @DatabaseField(id = true, canBeNull = false)
    UUID id;
    @DatabaseField(canBeNull = false)
    String name;
    @DatabaseField(canBeNull = false)
    Timestamp createTime;
    @DatabaseField(canBeNull = false)
    UUID playerUuid;

    @DatabaseField(canBeNull = false)
    TransitStatus status;

    @DatabaseField(canBeNull = false)
    String worldName;

    @ForeignCollectionField(eager = true)
    ForeignCollection<DbTransitConnector> connectors;

    @DatabaseField(canBeNull = false, foreign = true)
    DbRegion region;

    public DbTransitNode() {
    }
}
