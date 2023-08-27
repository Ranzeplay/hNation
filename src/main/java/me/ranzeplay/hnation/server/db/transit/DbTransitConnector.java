package me.ranzeplay.hnation.server.db.transit;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.joml.Vector3i;

import java.util.UUID;

/**
 * Connect Node and Line
 */
@DatabaseTable(tableName = "transitConnector")
public class DbTransitConnector {
    @DatabaseField(id = true, canBeNull = false)
    UUID id;

    @DatabaseField(foreign = true)
    DbTransitLine line;
    @DatabaseField(canBeNull = false, foreign = true)
    DbTransitNode node;
    @DatabaseField(canBeNull = false)
    int connectorId;

    @DatabaseField(canBeNull = false)
    TransitStatus status;

    // Use 2 points to mark the track of the platform
    @DatabaseField(canBeNull = false)
    int x1;
    @DatabaseField(canBeNull = false)
    int y1;
    @DatabaseField(canBeNull = false)
    int z1;

    @DatabaseField(canBeNull = false)
    int x2;
    @DatabaseField(canBeNull = false)
    int y2;
    @DatabaseField(canBeNull = false)
    int z2;

    public DbTransitConnector() {
    }

    public Vector3i getPoint1() {
        return new Vector3i(x1, y1, z1);
    }

    public Vector3i getPoint2() {
        return new Vector3i(x2, y2, z2);
    }

    public void setSegment(Vector3i p1, Vector3i p2) {
        x1 = p1.x();
        y1 = p1.y();
        z1 = p1.z();

        x2 = p2.x();
        y2 = p2.y();
        z2 = p2.z();
    }
}
