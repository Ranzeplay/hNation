package me.ranzeplay.hnation.server.db.transit;

import com.google.gson.Gson;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import org.joml.Vector3i;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

@DatabaseTable(tableName = "transitLine")
public class DbTransitLine {
    @DatabaseField(id = true, canBeNull = false)
    UUID id;
    @DatabaseField(canBeNull = false)
    String name;
    @DatabaseField(canBeNull = false)
    Timestamp createTime;
    @DatabaseField(canBeNull = false)
    UUID playerUuid;
    @DatabaseField(canBeNull = false)
    String worldName;

    @DatabaseField(canBeNull = false)
    TransitStatus status;

    @DatabaseField(canBeNull = true)
    String paths;

    @ForeignCollectionField(eager = true)
    ForeignCollection<DbTransitConnector> connectors;

    ArrayList<Vector3i> pathView;

    public DbTransitLine() {
    }

    public ArrayList<Vector3i> getPathView() {
        return pathView;
    }

    public void setPathView(ArrayList<Vector3i> pathView) {
        var gson = new Gson();
        paths = gson.toJson(pathView);

        this.pathView = pathView;
    }
}
