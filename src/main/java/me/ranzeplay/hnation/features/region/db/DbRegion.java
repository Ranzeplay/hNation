package me.ranzeplay.hnation.features.region.db;

import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import me.ranzeplay.hnation.features.player.db.DbPlayer;
import org.joml.Vector2i;
import org.joml.Vector3i;

import java.awt.*;
import java.awt.geom.Point2D;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

@DatabaseTable(tableName = "regions")
public class DbRegion {
    @DatabaseField(id = true, canBeNull = false)
    UUID id;

    @DatabaseField(canBeNull = false)
    int anchorX;
    @DatabaseField(canBeNull = false)
    int anchorY;
    @DatabaseField(canBeNull = false)
    int anchorZ;

    @DatabaseField(canBeNull = false)
    String border;

    @DatabaseField(canBeNull = false)
    int maxY;
    @DatabaseField(canBeNull = false)
    int minY;

    @DatabaseField(foreign = true)
    DbPlayer player;
    @DatabaseField(canBeNull = false)
    Timestamp timestamp;

    Polygon borderView;

    public DbRegion(UUID id, int maxY, int minY, DbPlayer player) {
        this.id = id;
        this.maxY = maxY;
        this.minY = minY;
        this.player = player;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Polygon getBorder() {
        return borderView;
    }

    public void setBorder(Polygon borderView) {
        var gson = new Gson();
        ArrayList<Vector2i> points = new ArrayList<>();
        for (int i = 0; i < borderView.npoints; i++) {
            points.add(new Vector2i(borderView.xpoints[i], borderView.ypoints[i]));
        }

        border = gson.toJson(points);

        this.borderView = borderView;
    }

    public Vector3i getAnchor() {
        return new Vector3i(anchorX, anchorY, anchorZ);
    }

    public void setAnchor(Vector3i anchor) {
        this.anchorX = anchor.x();
        this.anchorY = anchor.y();
        this.anchorZ = anchor.z();
    }

    public void updateAnchor() {
        var flatCenter = getCentroid(borderView);

        this.anchorX = (int) flatCenter.getX();
        this.anchorZ = (int) flatCenter.getY();
        this.anchorY = (maxY + minY) / 2;
    }

    /**
     * Get the centroid (geometric center) of a closed polygon.
     * The polygon must not be self intersecting, or this gives incorrect results.
     * Algorithm described here: http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/
     *
     * @param p
     * @return
     */
    private static Point2D getCentroid(Polygon polygon) {
        var points = new ArrayList<Point2D>();
        for (int i = 0; i < polygon.npoints; i++) {
            points.add(new Point(polygon.xpoints[i], polygon.ypoints[i]));
        }
        Point2D[] p = new Point2D[polygon.npoints];
        p = points.toArray(p);

        double cx = 0;
        double cy = 0;
        for (int i = 0; i < p.length; i++) {
            int j = (i + 1) % p.length;
            double n = ((p[i].getX() * p[j].getY()) - (p[j].getX() * p[i]
                    .getY()));
            cx += (p[i].getX() + p[j].getX()) * n;
            cy += (p[i].getY() + p[j].getY()) * n;
        }
        double a = getArea(p);
        double f = 1 / (a * 6d);
        cx *= f;
        cy *= f;
        return new Point2D.Double(cx, cy);
    }

    /**
     * Get the area of a closed polygon.
     * The polygon must not be self intersecting, or this gives incorrect results.
     * Algorithm described here: http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/
     *
     * @param p points that define the polygon
     * @return
     */
    private static double getArea(Point2D[] p) {
        double a = 0;
        for (int i = 0; i < p.length; i++) {
            int j = (i + 1) % p.length;
            a += (p[i].getX() * p[j].getY());
            a -= (p[j].getX() * p[i].getY());
        }
        a *= 0.5;
        return a;
    }

    public DbRegion() {
    }

    public UUID getId() {
        return id;
    }

    public DbPlayer getPlayer() {
        return player;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Polygon getBorderView() {
        return borderView;
    }
}
