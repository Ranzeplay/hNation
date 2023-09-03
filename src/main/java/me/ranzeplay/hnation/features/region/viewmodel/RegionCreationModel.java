package me.ranzeplay.hnation.features.region.viewmodel;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import org.joml.Vector2i;

import java.util.ArrayList;

public class RegionCreationModel {
    String name;
    String worldName;
    ArrayList<Vector2i> borders;
    int maxY;
    int minY;

    public RegionCreationModel(String name, String worldName, int maxY, int minY) {
        this.name = name;
        this.worldName = worldName;
        this.borders = new ArrayList<>();
        this.maxY = maxY;
        this.minY = minY;
    }

    public String getName() {
        return name;
    }

    public String getWorldName() {
        return worldName;
    }

    public ArrayList<Vector2i> getPoints() {
        return borders;
    }

    public void setPoints(ArrayList<Vector2i> borders) {
        this.borders = borders;
    }

    public void appendPoint(Vector2i point) {
        this.borders.add(point);
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public NbtCompound toNbt() {
        var nbt = new NbtCompound();
        nbt.putInt("maxY", maxY);
        nbt.putInt("minY", minY);
        nbt.putString("worldName", worldName);
        nbt.putString("name", name);

        var list = new NbtList();
        for (var point : this.borders){
            var pointNbt = new NbtCompound();
            pointNbt.putInt("x", point.x());
            pointNbt.putInt("y", point.y());

            list.add(pointNbt);
        }
        nbt.put("points", list);

        return nbt;
    }

    public static RegionCreationModel fromNbt(NbtCompound nbt) {
        var result = new RegionCreationModel(nbt.getString("name"), nbt.getString("worldName"), nbt.getInt("maxY"), nbt.getInt("minY"));

        var pointsNbt = nbt.getList("points", NbtElement.COMPOUND_TYPE);
        for(int i = 0; i < pointsNbt.size(); i++) {
            var pointComp = pointsNbt.getCompound(i);
            result.borders.add(new Vector2i(pointComp.getInt("x"), pointComp.getInt("y")));
        }

        return result;
    }
}
