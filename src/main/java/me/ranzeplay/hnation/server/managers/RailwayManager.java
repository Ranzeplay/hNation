package me.ranzeplay.hnation.server.managers;

import me.ranzeplay.hnation.general.GeneralMain;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.concurrent.CompletableFuture;

public class RailwayManager {
    public static void scanRailwayPath(ServerPlayerEntity player) {
        var pos = player.getSteppingPos().add(0, 1, 0);
        var state = player.getServerWorld().getBlockState(pos);
        var block = state.getBlock();
        if (block instanceof AbstractRailBlock) {
            CompletableFuture.runAsync(() -> {
                var scanner = new RailwayScanner(player.getWorld(), pos);
                scanner.scan();
                GeneralMain.LOGGER.info("A total of " + scanner.getScannedPositions().size() + " railway block(s) has found");
                scanner.simplifyPath();
                GeneralMain.LOGGER.info("Simplified to " + scanner.getScannedPositions().size() + " railway node(s)");
                GeneralMain.LOGGER.info(scanner.getScannedPositions().toString());
            });
        }
    }
}
