package me.ranzeplay.hnation.features.transit.server;

import me.ranzeplay.hnation.features.region.RegionManager;
import me.ranzeplay.hnation.features.transit.TransitLineManager;
import me.ranzeplay.hnation.features.transit.TransitNodeManager;
import me.ranzeplay.hnation.features.transit.db.DbTransitConnector;
import me.ranzeplay.hnation.features.transit.db.DbTransitNode;
import me.ranzeplay.hnation.features.transit.db.TransitStatus;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class ServerTransitNodeCommandHandler {
    private static void createNode(ServerPlayerEntity player, PacketByteBuf buf) {
        var name = buf.readString();
        var regionName = buf.readString();

        var region = RegionManager.getInstance().getRegion(regionName);
        var node = new DbTransitNode(name, player.getUuid(), TransitStatus.MAINTAINING, player.getWorld().getDimensionKey().getValue().toString(), region);
        TransitNodeManager.getInstance().create(node);

        player.sendMessage(Text.literal("Success"));
    }

    private static Vector3i convertVector(Vector3f vec) {
        return new Vector3i((int) vec.x, (int) vec.y, (int) vec.z);
    }

    private static void createConnector(ServerPlayerEntity player, PacketByteBuf buf) {
        var point1 = convertVector(buf.readVector3f());
        var point2 = convertVector(buf.readVector3f());
        var transitNodeName = buf.readString();
        var transitLineName = buf.readString();
        var connectorIndex = buf.readInt();

        var connector = new DbTransitConnector(TransitLineManager.getInstance().getTransitLine(transitLineName),
                TransitNodeManager.getInstance().getNode(transitNodeName),
                connectorIndex,
                TransitStatus.MAINTAINING,
                point1,
                point2);

        player.sendMessage(Text.literal("Success"));
    }
}
