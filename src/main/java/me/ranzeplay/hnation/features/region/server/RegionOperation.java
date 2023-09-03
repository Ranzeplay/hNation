package me.ranzeplay.hnation.features.region.server;

import me.ranzeplay.hnation.features.region.db.DbRegion;
import me.ranzeplay.hnation.features.region.viewmodel.RegionCreationModel;
import me.ranzeplay.hnation.main.NetworkingIdentifier;
import me.ranzeplay.hnation.main.ServerMain;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.awt.*;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class RegionOperation {
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

        ServerPlayNetworking.send(player, NetworkingIdentifier.CREATE_REGION_REPLY, PacketByteBufs.create());
    }
}
