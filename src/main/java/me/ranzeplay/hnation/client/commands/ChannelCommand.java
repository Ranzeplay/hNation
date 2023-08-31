package me.ranzeplay.hnation.client.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.UUID;

public class ChannelCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> buildCommandTree() {
        return ClientCommandManager.literal("channel")
                .then(ClientCommandManager.literal("create")
                        .then(ClientCommandManager.argument("name", StringArgumentType.string())
                        )
                )
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
                );
    }

    public static void create(String name) {
    }

    public static void join(UUID id) {
    }

    public static void leave(UUID id) {
    }

    public static void invite(String playerName) {
    }

    public static void kick(String playerName) {
    }

    public static void warn(String playerName, String reason) {
    }
}
