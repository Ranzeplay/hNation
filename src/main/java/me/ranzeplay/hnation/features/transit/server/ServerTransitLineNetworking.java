package me.ranzeplay.hnation.features.transit.server;

import me.ranzeplay.hnation.main.NetworkingIdentifier;
import me.ranzeplay.hnation.main.ServerMain;
import me.ranzeplay.hnation.features.transit.db.DbTransitLine;
import me.ranzeplay.hnation.features.transit.db.TransitStatus;
import me.ranzeplay.hnation.features.transit.utils.RailwayScanner;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.joml.Vector3i;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;

public class ServerTransitLineNetworking {
    public static void scanRailwayPath(ServerPlayerEntity player, PacketByteBuf buf) {
        var pos = player.getSteppingPos().add(0, 1, 0);
        var state = player.getServerWorld().getBlockState(pos);
        var block = state.getBlock();
        if (block instanceof AbstractRailBlock) {
            new Thread(() -> {
                var logger = LoggerFactory.getLogger("hNation-Railway");
                logger.info("Beginning railway scan");
                var scanner = new RailwayScanner(player.getWorld(), pos);
                scanner.scan();
                logger.info("A total of " + scanner.getScannedPositions().size() + " railway block(s) has found");
                scanner.simplifyPath();
                logger.info("Simplified to " + scanner.getScannedPositions().size() + " railway node(s)");

                var paths = scanner.getScannedPositions().stream().map(a -> new Vector3i(a.getX(), a.getY(), a.getZ())).toList();

                var model = new DbTransitLine(buf.readString(), player.getUuid(), player.getWorld().getDimensionKey().getValue().toString(), TransitStatus.MAINTAINING, new ArrayList<>(paths));
                try {
                    ServerMain.dbManager.getTransitLineDao().create(model);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    public static void registerEvents() {
        ServerPlayNetworking.registerGlobalReceiver(NetworkingIdentifier.CREATE_TRANSIT_LINE_REQUEST,
                (_minecraftServer, sender, _serverPlayNetworkHandler, packetByteBuf, _packetSender) -> {
                    ServerTransitLineNetworking.scanRailwayPath(sender, packetByteBuf);
                }
        );
    }
}
