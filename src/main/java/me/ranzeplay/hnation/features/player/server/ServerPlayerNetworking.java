package me.ranzeplay.hnation.features.player.server;

import me.ranzeplay.hnation.features.player.PlayerManager;
import me.ranzeplay.hnation.main.ServerMain;
import me.ranzeplay.hnation.features.player.db.DbPlayer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;

import java.sql.SQLException;

public class ServerPlayerNetworking {
    public static void onPlayerJoin(ServerPlayerEntity player) {
        PlayerManager.getInstance().createIfNotExists(player);
    }

    public static void registerEvents() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerNetworking.onPlayerJoin(handler.getPlayer());
        });
    }
}
