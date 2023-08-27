package me.ranzeplay.hnation.server.networking;

import me.ranzeplay.hnation.server.ServerMain;
import me.ranzeplay.hnation.server.db.communication.DbPublicMessage;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.sql.SQLException;

public class CommunicationOperation {

    public static void publicChat(MinecraftServer server, ServerPlayerEntity sender, PacketByteBuf packetByteBuf) throws SQLException {
        var message = packetByteBuf.readString();
        var player = ServerMain.dbManager
                .getPlayerDao()
                .queryForId(sender.getUuid());

        var model = new DbPublicMessage(message, player);
        ServerMain.dbManager.getPublicMessageDao().create(model);

        var players = server.getPlayerManager().getPlayerList();
        for (var p : players) {
            p.sendMessage(model.toMessageText());
        }
    }
}
