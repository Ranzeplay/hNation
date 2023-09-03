package me.ranzeplay.hnation.features.communication.channel.client;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.ranzeplay.hnation.main.ClientMain;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.PacketByteBuf;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public class ChannelCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> buildCommandTree() {
        return ClientCommandManager.literal("channel")
                .then(ClientCommandManager.literal("create"))
                .then(ClientCommandManager.literal("invite")
                        .then(ClientCommandManager.argument("playerName", StringArgumentType.string())
                        )
                )
                .then(ClientCommandManager.literal("kick")
                        .then(ClientCommandManager.argument("playerName", StringArgumentType.string())
                        )
                )
                .then(ClientCommandManager.literal("leave"))
                .then(ClientCommandManager.literal("warn")
                        .then(ClientCommandManager.argument("playerName", StringArgumentType.string())
                        )
                )
                .then(ClientCommandManager.literal("list"));
    }

    public static void create() {
        if(ClientMain.joinedSquad == null) {

        }
    }

    public static void join(UUID id) {
    }

    public static void leave(UUID id) {
    }

    public static void invite(String playerName) {
    }

    public static void kick(String playerName) {
    }
    public static void list() {}

    public static void warn(String playerName, String reason) {
    }

    public static void syncChannel(PacketByteBuf buf) {
    }
}
