package me.ranzeplay.hnation.server.managers;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.realityforge.vecmath.Vector3f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class RailwayScanner {
    World world;
    ArrayList<Vec3i> scannedPositions;
    Vec3i originPos;

    public RailwayScanner(World world, Vec3i originPos) {
        this.world = world;
        this.originPos = originPos;
        this.scannedPositions = new ArrayList<>();
    }

    public ArrayList<Vec3i> getScannedPositions() {
        return scannedPositions;
    }

    public void scan() {
        scanConnected(originPos, 0);

        // Sort the positions
        int borderIndex;
        for (borderIndex = 0; borderIndex < scannedPositions.size() - 1; borderIndex++) {
            if (scannedPositions.get(borderIndex) == null && scannedPositions.get(borderIndex + 1) == null) {
                break;
            }
        }

        borderIndex--;
        scannedPositions.removeIf(Objects::isNull);

        var l1 = new ArrayList<>(scannedPositions.subList(0, borderIndex));
        Collections.reverse(l1);
        var l2 = scannedPositions.stream().skip(borderIndex).toList();

        l1.addAll(l2);
        scannedPositions = l1;
    }

    private void scanConnected(Vec3i pos, int depth) {
        if(depth == 1) scannedPositions.add(null);

        if (!scannedPositions.contains(pos)) {
            var blockState = world.getBlockState(new BlockPos(pos));
            var block = blockState.getBlock();

            if (block instanceof AbstractRailBlock railBlock) {
                scannedPositions.add(pos);
                var shape = blockState.get(railBlock.getShapeProperty());
                switch (shape) {
                    case NORTH_SOUTH -> {
                        scanConnected(pos.north(), depth + 1);
                        scanConnected(pos.north().down(), depth + 1);
                        scanConnected(pos.south(), depth + 1);
                        scanConnected(pos.south().down(), depth + 1);
                    }
                    case EAST_WEST -> {
                        scanConnected(pos.east(), depth + 1);
                        scanConnected(pos.east().down(), depth + 1);
                        scanConnected(pos.west(), depth + 1);
                        scanConnected(pos.west().down(), depth + 1);
                    }
                    case ASCENDING_EAST -> {
                        scanConnected(pos.east().up(), depth + 1);
                        scanConnected(pos.west(), depth + 1);
                        scanConnected(pos.west().down(), depth + 1);
                    }
                    case ASCENDING_WEST -> {
                        scanConnected(pos.west().up(), depth + 1);
                        scanConnected(pos.east(), depth + 1);
                        scanConnected(pos.east().down(), depth + 1);
                    }
                    case ASCENDING_NORTH -> {
                        scanConnected(pos.north().up(), depth + 1);
                        scanConnected(pos.south(), depth + 1);
                        scanConnected(pos.south().down(), depth + 1);
                    }
                    case ASCENDING_SOUTH -> {
                        scanConnected(pos.south().up(), depth + 1);
                        scanConnected(pos.north(), depth + 1);
                        scanConnected(pos.north().down(), depth + 1);
                    }
                    case SOUTH_EAST -> {
                        scanConnected(pos.south(), depth + 1);
                        scanConnected(pos.south().down(), depth + 1);
                        scanConnected(pos.east(), depth + 1);
                        scanConnected(pos.east().down(), depth + 1);
                    }
                    case SOUTH_WEST -> {
                        scanConnected(pos.south(), depth + 1);
                        scanConnected(pos.south().down(), depth + 1);
                        scanConnected(pos.west(), depth + 1);
                        scanConnected(pos.west().down(), depth + 1);
                    }
                    case NORTH_WEST -> {
                        scanConnected(pos.north(), depth + 1);
                        scanConnected(pos.north().down(), depth + 1);
                        scanConnected(pos.west(), depth + 1);
                        scanConnected(pos.west().down(), depth + 1);
                    }
                    case NORTH_EAST -> {
                        scanConnected(pos.north(), depth + 1);
                        scanConnected(pos.north().down(), depth + 1);
                        scanConnected(pos.east(), depth + 1);
                        scanConnected(pos.east().down(), depth + 1);
                    }
                }
            }
        }
    }

    public void simplifyPath() {
        var result = new ArrayList<Vec3i>();

        // Convert all scanned positions to Vec3i
        var origin = new ArrayList<Vec3i>();
        for (var p : scannedPositions) {
            origin.add(new Vec3i(p.getX(), p.getY(), p.getZ()));
        }

        result.add(origin.get(0));
        result.add(origin.get(1));
        for (int i = 2; i < origin.size(); i++) {
            var resultLineVec = result.get(result.size() - 1).subtract(result.get(result.size() - 2));
            var appendVec = origin.get(i).subtract(result.get(result.size() - 1));
            if (sameDirection(resultLineVec, appendVec)) {
                result.set(result.size() - 1, origin.get(i));
            } else {
                result.add(origin.get(i));
            }
        }

        scannedPositions = result;
    }

    public boolean sameDirection(Vec3i v1, Vec3i v2) {
        Vector3f vv1 = new Vector3f(v1.getX(), v1.getY(), v1.getZ());
        Vector3f vv2 = new Vector3f(v2.getX(), v2.getY(), v2.getZ());

        var angle = vv1.angle(vv2);
        return Math.abs(angle) < 0.2;
    }
}
