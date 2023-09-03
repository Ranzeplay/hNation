package me.ranzeplay.hnation.features.communication.squad.client;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.ranzeplay.hnation.main.ClientMain;
import me.ranzeplay.hnation.main.NetworkingIdentifier;
import me.ranzeplay.hnation.features.player.db.DbPlayer;
import me.ranzeplay.hnation.features.communication.squad.db.DbSquad;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;

import java.util.Objects;
import java.util.UUID;

public class SquadCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> buildCommandTree() {
        return ClientCommandManager.literal("squad")
                .then(ClientCommandManager.literal("create")
                        .executes(context -> create())
                )
                .then(ClientCommandManager.literal("invite")
                        .then(ClientCommandManager.argument("playerName", StringArgumentType.string())
                                .executes(context -> invite(StringArgumentType.getString(context, "playerName"))
                                )
                        ))
                .then(ClientCommandManager.literal("kick")
                        .then(ClientCommandManager.argument("playerName", StringArgumentType.string())
                                .executes(context -> kick(StringArgumentType.getString(context, "playerName"))
                                )
                        ))
                .then(ClientCommandManager.literal("leave")
                        .executes(context -> leave())
                )
                .then(ClientCommandManager.literal("dismiss")
                        .executes(context -> dismiss())
                )
                .then(ClientCommandManager.literal("warn")
                        .then(ClientCommandManager.argument("playerName", StringArgumentType.string())
                                .then(ClientCommandManager.argument("reason", StringArgumentType.string())
                                        .executes(context ->
                                                warn(StringArgumentType.getString(context, "playerName"),
                                                        StringArgumentType.getString(context, "reason")
                                                )
                                        )
                                )
                        )
                );
    }

    public static int create() {
        if(ClientMain.joinedSquad == null) {
            var player = Objects.requireNonNull(MinecraftClient.getInstance().player);
            var squad = new DbSquad(new DbPlayer(player.getUuid(), player.getEntityName()));

            ClientPlayNetworking.send(NetworkingIdentifier.SQUAD_CREATE_REQUEST, PacketByteBufs.create().writeNbt(squad.toNbt()));
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int join(UUID id) {
        return Command.SINGLE_SUCCESS;
    }

    public static int leave() {
        return Command.SINGLE_SUCCESS;
    }

    public static int invite(String playerName) {
        return Command.SINGLE_SUCCESS;
    }

    public static int dismiss() {
        return Command.SINGLE_SUCCESS;
    }

    public static int kick(String playerName) {
        return Command.SINGLE_SUCCESS;
    }

    public static int warn(String playerName, String reason) {
        return Command.SINGLE_SUCCESS;
    }
}
