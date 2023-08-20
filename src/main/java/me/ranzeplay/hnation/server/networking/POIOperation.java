package me.ranzeplay.hnation.server.networking;

import me.ranzeplay.hnation.networking.POICreationModel;
import me.ranzeplay.hnation.server.ServerMain;
import me.ranzeplay.hnation.server.db.DbPOI;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

public class POIOperation {
    public static void create(ServerPlayerEntity sender, PacketByteBuf buf) throws SQLException {
        var packet = POICreationModel.fromNbt(Objects.requireNonNull(buf.readNbt()));

        // Check if already exists
        var existingPoi = ServerMain.dbManager.getPoiDao()
                .queryBuilder()
                .where()
                .eq("name", packet.name())
                .queryForFirst();

        if(existingPoi != null) {
            sender.sendMessage(Text.of("Invalid name"));
            return;
        }

        // Create POI
        var poi = new DbPOI(UUID.randomUUID(), packet.name(), new Timestamp(System.currentTimeMillis()), packet.playerUuid(), packet.worldName(), packet.x(), packet.y(), packet.z());
        ServerMain.dbManager.getPoiDao().create(poi);

        sender.sendMessage(Text.of("POI Created"));
    }
}
