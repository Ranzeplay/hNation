package me.ranzeplay.hnation.features.region.server;

import me.ranzeplay.hnation.features.region.db.DbRegion;
import me.ranzeplay.hnation.features.region.viewmodel.RegionCreationModel;
import me.ranzeplay.hnation.main.ServerMain;
import me.ranzeplay.hnation.networking.RegionIdentifier;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.awt.*;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class ServerRegionNetworking {
    public static void create(ServerPlayerEntity player, PacketByteBuf buf) throws SQLException {
        var dao = ServerMain.dbManager.getRegionDao();

        var dbPlayer = ServerMain.dbManager.getPlayerDao().queryForId(player.getUuid());

        var model = RegionCreationModel.fromNbt(Objects.requireNonNull(buf.readNbt()));
        var region = new DbRegion(UUID.randomUUID(), model.getMaxY(), model.getMinY(), dbPlayer);

        var polygon = new Polygon();
        for (var point : model.getPoints()) {
            polygon.addPoint(point.x(), point.y());
        }

        region.setBorder(polygon);
        region.updateAnchor();

        dao.create(region);

        ServerPlayNetworking.send(player, RegionIdentifier.CREATE_REGION_REPLY, PacketByteBufs.create());
    }

    public static void registerEvents() {
        ServerPlayNetworking.registerGlobalReceiver(RegionIdentifier.CREATE_REGION_REQUEST,
                (_minecraftServer, sender, _serverPlayNetworkHandler, packetByteBuf, _packetSender) -> {
                    try {
                        ServerRegionNetworking.create(sender, packetByteBuf);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
}
