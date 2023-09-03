package me.ranzeplay.hnation.features.poi.server;

import me.ranzeplay.hnation.main.NetworkingIdentifier;
import me.ranzeplay.hnation.features.poi.viewmodel.POICreationModel;
import me.ranzeplay.hnation.features.poi.viewmodel.POIViewModel;
import me.ranzeplay.hnation.features.poi.viewmodel.POIQueryViewModel;
import me.ranzeplay.hnation.main.ServerMain;
import me.ranzeplay.hnation.features.poi.db.DbPOI;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
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

    public static void query(ServerPlayerEntity sender) {
        var list = new ArrayList<POIViewModel>();
        for(var poi : ServerMain.dbManager.getPoiDao()){
            list.add(new POIViewModel(poi));
        }

        var model = new POIQueryViewModel(list.toArray(new POIViewModel[0]));

        ServerPlayNetworking.send(sender, NetworkingIdentifier.QUERY_POI_REPLY, PacketByteBufs.create().writeNbt(model.toNbt()));
    }
}
