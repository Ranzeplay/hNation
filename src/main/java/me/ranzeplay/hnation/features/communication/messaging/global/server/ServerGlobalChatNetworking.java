package me.ranzeplay.hnation.features.communication.messaging.global.server;

import me.ranzeplay.hnation.features.communication.messaging.global.db.DbGlobalMessage;
import me.ranzeplay.hnation.main.NetworkingIdentifier;
import me.ranzeplay.hnation.main.ServerMain;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.sql.SQLException;

public class ServerGlobalChatNetworking {
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

    public static void registerEvents() {
        ServerPlayNetworking.registerGlobalReceiver(NetworkingIdentifier.SEND_CHAT_PUBLIC,
                (minecraftServer, sender, _serverPlayNetworkHandler, packetByteBuf, _packetSender) -> {
                    try {
                        ServerGlobalChatNetworking.send(minecraftServer, sender, packetByteBuf);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
}
