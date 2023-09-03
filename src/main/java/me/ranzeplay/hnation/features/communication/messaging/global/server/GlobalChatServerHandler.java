package me.ranzeplay.hnation.features.communication.messaging.global.server;

import me.ranzeplay.hnation.features.communication.messaging.global.db.DbGlobalMessage;
import me.ranzeplay.hnation.main.ServerMain;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.sql.SQLException;

public class GlobalChatServerHandler {
    public static void send(MinecraftServer server, ServerPlayerEntity sender, PacketByteBuf packetByteBuf) throws SQLException {
        var message = packetByteBuf.readString();
        var player = ServerMain.dbManager
                .getPlayerDao()
                .queryForId(sender.getUuid());

        var model = new DbGlobalMessage(message, player);
        ServerMain.dbManager.getPublicMessageDao().create(model);

        var players = server.getPlayerManager().getPlayerList();
        for (var p : players) {
            p.sendMessage(model.toMessageText());
        }
    }
}
